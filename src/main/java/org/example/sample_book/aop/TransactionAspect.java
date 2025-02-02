package org.example.sample_book.aop;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.List;

@Configuration
public class TransactionAspect {
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Bean
    TransactionInterceptor transactionAdvice() {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(transactionManager);

        MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
        RuleBasedTransactionAttribute txAttr = new RuleBasedTransactionAttribute();
        txAttr.setName("*");
        txAttr.setRollbackRules(List.of(new RollbackRuleAttribute(Exception.class)));
        source.setTransactionAttribute(txAttr);

        interceptor.setTransactionAttributeSource(source);
        return interceptor;
    }
    @Bean
    Advisor transactionAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* sample_book..service.*Impl.*(..))");
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
    }
}
