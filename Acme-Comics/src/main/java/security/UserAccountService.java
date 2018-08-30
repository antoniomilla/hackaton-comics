
package security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import exceptions.ResourceNotUniqueException;
import utilities.CheckUtils;

@Service
@Transactional
public class UserAccountService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserAccountRepository repository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public UserAccountService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public UserAccount create(final String username, final String password, final String authority) throws ResourceNotUniqueException
	{
		if (this.findByUsername(username) != null) {
			throw new ResourceNotUniqueException();
		}

		UserAccount account = new UserAccount();
		account.setUsername(username);
		account.setPassword(new Md5PasswordEncoder().encodePassword(password, null));

		final Collection<Authority> authorities = new ArrayList<Authority>();
		final Authority auth = new Authority();
		auth.setAuthority(authority);
		authorities.add(auth);
		account.setAuthorities(authorities);
		account = this.repository.save(account);

		Assert.notNull(account);
		return account;
	}

	public UserAccount updatePassword(final UserAccount userAccount, final String password) {
		CheckUtils.checkExists(userAccount);
		final UserAccount currentAccount = this.findOne(userAccount.getId());
		CheckUtils.checkSameVersion(userAccount, currentAccount);

		currentAccount.setPassword(new Md5PasswordEncoder().encodePassword(password, null));
		return this.repository.save(currentAccount);
	}

	public boolean passwordMatchesAccount(final UserAccount account, final String password) {
		return new Md5PasswordEncoder().isPasswordValid(account.getPassword(), password, null);
	}

	public Collection<UserAccount> findAll() {
		return this.repository.findAll();
	}
	public UserAccount findOne(final int userAccountId) {
		return this.repository.findOne(userAccountId);
	}

	public UserAccount save(final UserAccount userAccount) {

		return this.repository.save(userAccount);

	}
	public void delete(final UserAccount userAccount) {
		this.repository.delete(userAccount);
	}

	public UserAccount findByUsername(final String username) {
		return this.repository.findByUsername(username);
	}
	// Other business methods -------------------------------------------------

}
