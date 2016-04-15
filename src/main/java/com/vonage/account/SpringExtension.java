package com.vonage.account;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;

/**
 * The Extension implementation.
 */
public class SpringExtension implements Extension {
  private volatile ApplicationContext applicationContext;
  public static final SpringExtensionFactory factory = new SpringExtensionFactory();

  private SpringExtension() {}

  /**
   * Used to initialize the Spring application context for the extension.
   * @param applicationContext
   */
  public void initialize(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Create a Props for the specified actorBeanName using the
   * SpringActorProducer class.
   *
   * @param actorBeanName  The name of the actor bean to create Props for
   * @return a Props that will create the named actor bean using Spring
   */
  public Props props(String actorBeanName) {
    return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
  }

  /**
   * An Akka Extension to provide access to Spring managed Actor Beans.
   */
  public static class SpringExtensionFactory extends AbstractExtensionId<SpringExtension> {
    private SpringExtensionFactory() {}

    /**
     * Is used by Akka to instantiate the Extension identified by this
     * ExtensionId, internal use only.
     */
    @Override
    public SpringExtension createExtension(ExtendedActorSystem system) {
      return new SpringExtension();
    }
  }
}
