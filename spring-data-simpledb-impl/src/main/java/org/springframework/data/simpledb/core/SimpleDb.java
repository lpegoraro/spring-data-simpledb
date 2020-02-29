package org.springframework.data.simpledb.core;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.simpledb.core.domain.DomainManagementPolicy;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

/**
 * A configuration class to create and instance of {@link AmazonSimpleDB} from user credentials and to hold few extra
 * configuration options: {@link DomainManagementPolicy},
 * {@link org.springframework.data.simpledb.annotation.DomainPrefix}, consistentRead and dev
 */
public class SimpleDb implements InitializingBean {

	private AmazonSimpleDB simpleDbClient;

	private final String accessID;
	private final String secretKey;
	private final String domainPrefix;

	private DomainManagementPolicy domainManagementPolicy = DomainManagementPolicy.UPDATE;
	private boolean consistentRead = false;

	private int unavailableServiceRetries = 1;
	private SimpleDbDomain simpleDbDomain = new SimpleDbDomain();

	public SimpleDb(String accessID, String secretKey, String domainPrefix) {
		// to be used in Java configuration when bean is not read from config
		this.accessID = accessID;
		this.secretKey = secretKey;
		this.domainPrefix = domainPrefix;
	}

	public AmazonSimpleDB getSimpleDbClient() {
		return simpleDbClient;
	}

	/**
	 * Set the domain management policy. This can be one of:
	 * <ul>
	 * <li><b>DROP_CREATE</b>: The domain will be dropped if it exists and created again
	 * <li><b>UPDATE</b>: The domain will be created if it does not exist, this is the default policy
	 * <li><b>NONE</b>: No action taken, domain must be created by separate API call
	 * </ul>
	 * 
	 * @param domainManagementPolicy
	 */
	public void setDomainManagementPolicy(DomainManagementPolicy domainManagementPolicy) {
		this.domainManagementPolicy = domainManagementPolicy;
	}

	public DomainManagementPolicy getDomainManagementPolicy() {
		return this.domainManagementPolicy;
	}

	public String getDomainPrefix() {
		return this.domainPrefix;
	}

	public boolean isConsistentRead() {
		return consistentRead;
	}

	/**
	 * Amazon SimpleDB uses an eventual consistency model by default. This means
	 * a <i>create</i> or an <i>update</i> followed by an immediate <i>select</i>
	 * may not reflect the changes. By setting a <tt>true</tt> value, this can
	 * be avoided, however, this carries a performance penalty as every create and
	 * update is followed by an implicit consistent select operation; defaults to 
	 * <b>false</b>.
	 * 
	 * @param consistentRead
	 */
	public void setConsistentRead(boolean consistentRead) {
		this.consistentRead = consistentRead;
	}

	public SimpleDbDomain getSimpleDbDomain() {
		return simpleDbDomain;
	}

	/**
	 * Set the number of attempts to retry ALL SimpleDB operations in case of
	 * service unavailability. Defaults to 1, which means, operations are NOT
	 * retried by default.
	 * 
	 * @param unavailableServiceRetries
	 */
	public void setUnavailableServiceRetries(int unavailableServiceRetries) {
		this.unavailableServiceRetries = unavailableServiceRetries;
	}

	public int getUnavailableServiceRetries() {
		return unavailableServiceRetries;
	}

	@Override
	public final void afterPropertiesSet() {
		final AWSCredentials awsCredentials = new AWSCredentials() {

			@Override
			public String getAWSAccessKeyId() {
				return accessID;
			}

			@Override
			public String getAWSSecretKey() {
				return secretKey;
			}
		};

		this.simpleDbClient = new AmazonSimpleDBClient(awsCredentials);

		simpleDbDomain = new SimpleDbDomain(domainPrefix);
	}

	public String getDomain(Class<?> clazz) {
		return simpleDbDomain.getDomain(clazz);
	}

}
