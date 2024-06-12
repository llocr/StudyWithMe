package december.spring.studywithme.utils;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

public abstract class MonkeyUtils {
	public static FixtureMonkey commonMonkey() {
		return FixtureMonkey.builder()
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build();
	}
	
	public static FixtureMonkey validMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.build();
	}
}
