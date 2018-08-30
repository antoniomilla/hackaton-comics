package controllers;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Actor;
import domain.User;
import services.ActorService;
import services.UserService;

public class RequestInterceptor implements HandlerInterceptor {
    @Autowired private ActorService actorService;
    @Autowired private UserService userService;
    @PersistenceContext private EntityManager entityManager;

    private static AtomicBoolean firstRequest = new AtomicBoolean(false);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (!firstRequest.get() && !firstRequest.getAndSet(true)) {
            onFirstRequest();

        }

        Actor principal = actorService.findPrincipal();
        if (principal instanceof User) {
            // Redirect blocked users to the blocked page.
            if (((User) principal).getBlocked() && !request.getServletPath().equals("/welcome/blocked.do")) {
                response.sendRedirect(request.getContextPath()+"/welcome/blocked.do");
                return false;
            }

            // Update user level if required.
            userService.updateUserLevelIfRequired((User) principal);
        }

        return true;
    }

    private void onFirstRequest() throws InterruptedException
    {
        // First request for this run of the webapp.
        // We cannot build the Lucene index in PopulateDatabase for the pre-production scenario
        // because it's not necessarily run there. We do need to run it for that scenario because
        // the tables do not start empty: At the very least Administrators has one entity that must be indexed.
        // Check if an index should be built right now.

        // Search for all actors, if we get none (at a minimum one administrator should always exist), there is no index built yet.
        FullTextEntityManager ftem = Search.getFullTextEntityManager(entityManager);
        Query q = ftem.getSearchFactory().buildQueryBuilder().forEntity(Actor.class).get().all().createQuery();
        if (ftem.createFullTextQuery(q).setMaxResults(1).getResultList().isEmpty()) {
            // Index rebuild required.
            ftem.createIndexer().startAndWait();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {

    }
}
