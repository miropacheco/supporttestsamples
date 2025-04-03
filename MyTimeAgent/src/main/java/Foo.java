import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.security.ProtectionDomain;

import net.bytebuddy.*;
import net.bytebuddy.agent.*;
import net.bytebuddy.asm.*;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;


class Foo {
    @Advice.OnMethodExit
    public static void currentTimeMillis(@Advice.Return(readOnly = false) long x) {
        x = 123;
    }

    public static void main(String[] args) {
        new AgentBuilder.Default()
        .enableNativeMethodPrefix("wmsnative")	
                .with(new ByteBuddy().with(Implementation.Context.Disabled.Factory.INSTANCE))
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
              
                .type(ElementMatchers.named("java.lang.System"))
                .transform(new AgentBuilder.Transformer() {
           
					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,
							ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
						// TODO Auto-generated method stub
						return builder.method(ElementMatchers.named("currentTimeMillis")).intercept(Advice.to(Foo.class).wrap(StubMethod.INSTANCE));
					}

                }).installOn(ByteBuddyAgent.install());

        System.out.println(System.currentTimeMillis());
       
    }
}