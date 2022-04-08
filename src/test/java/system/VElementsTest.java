package system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import visualElements.VElement;

public class VElementsTest {
	@Test
	void initConfigTest(){
		VElement vElement = new VElement();
		String key = "key";
		double init = Math.random()*1000;
		double lower = Math.random()*1000;
		double upper = Math.random()*1000;
		double step = Math.random()*1000;
		boolean editAble = Math.random() > 0.5;
		vElement.initConfigElement(key,init,lower,upper,step,editAble);

		Assertions.assertEquals(init,vElement.getConfigAbleList().get(key));
		Assertions.assertEquals(lower,vElement.getLimits().get(key).getKey());
		Assertions.assertEquals(upper,vElement.getLimits().get(key).getValue());
		Assertions.assertEquals(step,vElement.getStepMap().get(key));
		Assertions.assertEquals(editAble,vElement.getConfigAble().get(key));
	}
	@Test
	void updateConfigTest(){
		VElement vElement = new VElement();
		String key = "key";
		double init = Math.random()*1000;
		double lower = Math.random()*1000;
		double upper = Math.random()*1000;
		double random = Math.random()*1000+1000;
		vElement.initConfigElement(key,init,lower,upper);
		Assertions.assertEquals(init,vElement.getConfigAbleList().get(key));
		vElement.updateConfig(key,random);
		Assertions.assertEquals(random,vElement.getConfigAbleList().get(key));
	}
}
