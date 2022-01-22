package ink;

import org.apache.ignite.springdata22.repository.support.IgniteRepositoryFactory;
import org.apache.ignite.springdata22.repository.support.IgniteRepositoryFactoryBean;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.EventPublishingRepositoryProxyPostProcessor;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.ExtensionAwareQueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.util.Lazy;

import java.io.Serializable;
import java.util.Optional;

public class INKRepositoryFactoryBean<T extends Repository<V, K>, V, K extends Serializable> extends RepositoryFactoryBeanSupport<T, V, K> implements InitializingBean, ApplicationContextAware, BeanClassLoaderAware,
        BeanFactoryAware, ApplicationEventPublisherAware {

    private ApplicationContext ctx;
    private Optional<Object> customImplementation = Optional.empty();
    private Optional<RepositoryComposition.RepositoryFragments> repositoryFragments = Optional.empty();
    private Optional<QueryMethodEvaluationContextProvider> evaluationContextProvider = Optional.empty();
    private NamedQueries namedQueries;
    private QueryLookupStrategy.Key queryLookupStrategyKey;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private ApplicationEventPublisher publisher;
    private final Class<? extends T> repositoryInterface;

    /**
     * @param repoInterface Repository interface.
     */
    protected INKRepositoryFactoryBean(Class<? extends T> repoInterface) {
        super(repoInterface);
        this.repositoryInterface = repoInterface;
    }

    @Override
    public void setCustomImplementation(@NotNull Object customImplementation) {
        this.customImplementation = Optional.of(customImplementation);
        super.setCustomImplementation(customImplementation);
    }

    @Override
    public void setRepositoryFragments(RepositoryComposition.@NotNull RepositoryFragments repositoryFragments) {
        this.repositoryFragments = Optional.of(repositoryFragments);
        super.setRepositoryFragments(repositoryFragments);
    }

    @Override
    public void afterPropertiesSet() {
        //do nothing
    }
    public void setEvaluationContextProvider(QueryMethodEvaluationContextProvider evaluationContextProvider) {
        this.evaluationContextProvider = Optional.of(evaluationContextProvider);
    }
    public void setQueryLookupStrategyKey(QueryLookupStrategy.Key queryLookupStrategyKey) {
        this.queryLookupStrategyKey = queryLookupStrategyKey;
    }
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        this.beanFactory = beanFactory;

        if (!this.evaluationContextProvider.isPresent() && ListableBeanFactory.class.isInstance(beanFactory)) {
            this.evaluationContextProvider = Optional
                    .of(new ExtensionAwareQueryMethodEvaluationContextProvider((ListableBeanFactory) beanFactory));
        }
    }


    public void setNamedQueries(NamedQueries namedQueries) {
        this.namedQueries = namedQueries;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }


    private INKIgniteRepositoryFactory createFactory(String cacheName) {
        final var ret = new INKIgniteRepositoryFactory(ctx, repositoryInterface, cacheName, false);
        ret.setQueryLookupStrategyKey(queryLookupStrategyKey);
        ret.setNamedQueries(namedQueries);
        ret.setEvaluationContextProvider(
                evaluationContextProvider.orElseGet(() -> QueryMethodEvaluationContextProvider.DEFAULT));
        ret.setBeanClassLoader(classLoader);
        ret.setBeanFactory(beanFactory);

        if (publisher != null) {
            ret.addRepositoryProxyPostProcessor(new EventPublishingRepositoryProxyPostProcessor(publisher));
        }
        return ret;
    }

    public SampleRepository getRepo(String cacheName) {
        RepositoryComposition.RepositoryFragments customImplementationFragment = customImplementation
                .map(RepositoryComposition.RepositoryFragments::just)
                .orElseGet(RepositoryComposition.RepositoryFragments::empty);

        RepositoryComposition.RepositoryFragments repositoryFragmentsToUse = this.repositoryFragments
                .orElseGet(RepositoryComposition.RepositoryFragments::empty)
                .append(customImplementationFragment);

        return Lazy.of(() -> createFactory(cacheName).getRepository(SampleRepository.class, repositoryFragmentsToUse)).get();
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext ctx) throws BeansException {
        this.ctx = ctx;
    }

    @Override
    protected @NotNull RepositoryFactorySupport createRepositoryFactory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityInformation<V, K> getEntityInformation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RepositoryInformation getRepositoryInformation() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public T getObject() {
        throw new UnsupportedOperationException();
    }
}
