package org.sagesource.test.client;

import org.sagesource.simplerpc.client.ClientContext;
import org.sagesource.simplerpc.client.filter.ClientFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p></p>
 * <pre>
 *     author      XueQi
 *     date        2018/7/9
 *     email       job.xueqi@outlook.com
 * </pre>
 */
public class TestFilter2 extends ClientFilter {
	private static Logger LOGGER = LoggerFactory.getLogger(TestFilter2.class);

	@Override
	protected void doBeforeFilter(ClientContext clientContext) {

	}

	@Override
	protected void doPostFilter(ClientContext clientContext) {
		LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> after filter");
	}
}
