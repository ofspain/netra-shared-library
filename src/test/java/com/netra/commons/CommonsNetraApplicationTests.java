package com.netra.commons;

import com.netra.commons.util.BasicUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class CommonsNetraApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testHashingId(){
		Long id = 6l;
		String hashed = BasicUtil.encodeUrlBoundId(id);
	}

}
