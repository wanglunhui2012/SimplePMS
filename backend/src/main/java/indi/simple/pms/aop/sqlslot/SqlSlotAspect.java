package indi.simple.pms.aop.sqlslot;

import indi.simple.pms.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 21:33
 * @Description:
 */
@Aspect
@Component
public class SqlSlotAspect {
    /*@Autowired
    private ApplicationContext applicationContext;*/
    @Autowired
    private SqlSlotExpressionOperations sqlSlotExpressionOperations;
    private StandardEvaluationContext standardEvaluationContext=new StandardEvaluationContext();
    private ExpressionParser expressionParser=new SpelExpressionParser();
    //private static final ExpressionParser EXPRESSION_PARSER=new SpelExpressionParser();
    //private static final StandardEvaluationContext STANDARD_EVALUATION_CONTEXT=new StandardEvaluationContext();

    @PostConstruct
    public void init() {
        //standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        standardEvaluationContext.setRootObject(sqlSlotExpressionOperations);

        /*Class<?> clz=SqlSlotExpressionOperations.class;
        Method[] methods=clz.getMethods();
        for (Method method:methods){
            STANDARD_EVALUATION_CONTEXT.registerFunction(method.getName(),method);
        }
        System.out.println(EXPRESSION_PARSER.parseExpression("#{sqlSlotExpressionOperations.dataScope()}").getValue(STANDARD_EVALUATION_CONTEXT));*/

        /*AnnotationConfigServletWebServerApplicationContext annotationConfigServletWebServerApplicationContext=(AnnotationConfigServletWebServerApplicationContext) applicationContext;
        ConfigurableListableBeanFactory configurableBeanFactory=annotationConfigServletWebServerApplicationContext.getBeanFactory();
        System.out.println(configurableBeanFactory.getBeanExpressionResolver().evaluate("#{sqlSlotExpressionOperations.dataScope()}",new BeanExpressionContext(configurableBeanFactory, null)));*/
    }

    @Pointcut("@annotation(indi.simple.pms.aop.sqlslot.SqlSlot)")
    public void sqlSlotPointCut() {
    }

    @Before("sqlSlotPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature)signature;
        Method method = methodSignature.getMethod();
        SqlSlot sqlSlotAnnotation = method.getAnnotation(SqlSlot.class);

        String sqlSlot=expressionParser.parseExpression(sqlSlotAnnotation.value()).getValue(standardEvaluationContext,String.class);

        if (StringUtil.isNotBlank(sqlSlot)) {
            Object object = joinPoint.getArgs()[0];
            Class<?> clz = object.getClass();
            Field field = clz.getDeclaredField("sqlSlot");
            field.setAccessible(true);
            field.set(object, " and (" + sqlSlot.substring(4) + ")"); // 前面的" or "去掉
        }
    }
}
