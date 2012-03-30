package edu.uib.info323.image.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import junit.framework.TestCase;
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration("classpath:test-context.xml")
public abstract class AbstractCollosTest extends TestCase {

}
