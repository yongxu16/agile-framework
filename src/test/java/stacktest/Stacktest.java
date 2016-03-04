package stacktest;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

import org.junit.Test;

/**
 * 测试 栈 相关操作，
 * 场景： 自定义上下文
 * @author hanyx
 * @since
 */
public class Stacktest {
	
//	private static final Logger logger = LogManager.getLogger(Stacktest.class);

	
	@Test
	public void testDeque() {
		Deque<Integer> deque = new ArrayDeque<>() ;
		deque.push(111);
		System.out.println(deque);
		deque.push(222);
		System.out.println(deque);
		deque.push(333);
		System.out.println(deque);
		deque.pop() ;
		System.out.println(deque);
		System.out.println("peek -> " + deque.peek()); ;
//		System.out.println(deque.);
		deque.pop() ;
		System.out.println(deque);
		deque.pop() ;
		System.out.println(deque);
	}
	
	@Test
	public void testStack() {
		Stack<Integer> stack = new Stack<Integer>() ;
		stack.push(111);
		System.out.println(stack);
		stack.push(222);
		System.out.println(stack);
		stack.push(333);
		System.out.println(stack);
		stack.pop() ;
		System.out.println(stack);
		System.out.println("peek -> " + stack.peek()); ;
		System.out.println("search - > " + stack.search(222));
		stack.pop() ;
		System.out.println(stack);
		stack.pop() ;
		System.out.println(stack);
	}
	
}
