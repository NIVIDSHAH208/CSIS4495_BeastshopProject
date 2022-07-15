package com.beastshop.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.beastshop.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ProductRepositoryTest {
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	public void testFindByAlias() {
		String alias="canon-eos-m50";
		Product productByAlias = productRepository.findByAlias(alias);
		assertThat(productByAlias).isNotNull();
	}

}
