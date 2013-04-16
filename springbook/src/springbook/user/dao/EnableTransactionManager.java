package springbook.user.dao;

import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;

@Import(TransactionManagementConfigurationSelector.class)
public @interface EnableTransactionManager {
}
