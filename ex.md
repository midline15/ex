---
marp: true
---

# Spring 프레임워크

- 등장한 이유
- Bean이 뭐야?
- Bean은 관리된다.

---

# Spring FW 등장한 이유

- EJB(Enterprise JavaBeans)
    - EJB는 애플리케이션 작성을 쉽게 해준다.
    - EJB는 선언적 프로그래밍 모델
    - 트랜젝션, 보안, 분산컴퓨팅 이런것들을 굉장히 쉽게 할 수 있다.
    - EJB를 구동시킬 수 있는 Web Application Server가 등장
    - EJB 너무 싫다. (개발도 힘들고, 배포도 힘들고......)

---

# Expert one-on-one: J2EE Design and Devlopment

- 책 제목
- 로드 존슨(Rod Johnson)
  - Spring을 소개한다.
  - EJB에서 했던 일들을 더욱 간단한 Java Bean이 처리할 수 있게 된다.

---

# Bean이란?

- Java에서 인스턴스 생성
  - 프로그래머가 직접 인스턴스를 생성함.

```
    Book book = new Book();
```
- Bean은 컨테이너가 관리하는 객체
  - 객체의 생명주기를 컨테이너가 관리한다.
  - 객체를 싱글턴으로 만들 것인지, 프로토타입으로 만들 것인지

---

# 스프링의 핵심 기능 1

- 관점 지향 컨테이너
  - 빈을 생성, 관리한다.
  - 관점지향(AOP, aspect-oriented programming)

---

# Book.java

```
public class Book {
    private String title; // title 인스턴스 field(속성)
    private int price; // price 인스턴스 field
    
    //Book생성자
    public Book(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
```

---

# Book클래스의 인스턴스 생성

```
Book book1 = new Book("즐거운 자바", 20000);
```

1) new Book("즐거운 자바", 20000);
   - 생성자가 호출되면 Heap메모리에 인스턴스가 생성된다.
2) book1 은 1)에서 생성한 인스턴스를 참조한다.
   - book1 참조변수
   
---

# 프로그래머가 직접 인스턴스를 생성 사용

```
public class BookExam01 {
    public static void main(String[] args) {
        Book book = new Book("Java", 10000);
        System.out.println(book.getTitle());
        System.out.println(book.getPrice());
    }
}
```

---

# Bean을 만들 때 규칙
- 기본 생성자가 있어야 한다.


---

# 객체를 싱글턴으로 생성해주고, 자기자신도 싱글턴인 ApplicationContext
  
  - 컨테이너 역할을 수행한다.
  - Spring 이 제공하는 컨테이너는 이것보다 훨씬 복잡한 일을 한다.


```
package exam;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApplicationContext {
    // 1. 싱글턴 패턴 적용 - 자기 자신을 참조하는 static 필드를 선언한다. 바로 초기화
    private static ApplicationContext instance = new ApplicationContext();

    // 3. 1.에서 만든 인스턴스를 반환하는 static메소드를 만든다.
    public static ApplicationContext getInstance() {
        return instance;
    }

    private Properties props;
    private Map objectMap;

    // 2. 싱글턴 패턴 적용 - 생성자를 private으로 바꾼다.
    private ApplicationContext() {
        props = new Properties();
        objectMap = new HashMap<String, Object>();
        try {
            props.load(new FileInputStream("src/main/resources/Beans.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String id) throws Exception {
        Object o1 = objectMap.get(id);
        if (o1 == null) {
            String className = props.getProperty(id);
            // class name에 해당하는 문자열을 가지고 인스턴스를 생성할 수 있다.
            // Class.forName(className)은 CLASSPATH부터 className에 해당하는 클래스를 찾은 후
            // 그 클래스 정보를 반환한다.
            Class clazz = Class.forName(className);
       
            // ClassLoader를 이용한 인스턴스 생성. 기본생성자가 있어야 한다.
            Object o = clazz.newInstance(); // clazz 정보를 이용해 인스턴스를 생성한다.
            objectMap.put(id, o);
            o1 = objectMap.get(id);
        }
        return o1;
    }
}
```

---

```
package exam;

public class ApplicationContextMain {
    public static void main(String[] args) throws Exception{
        ApplicationContext context = ApplicationContext.getInstance();
        
        Book book = (Book) context.getBean("book1"); // id에 해당하는 인스턴스를 달라.
        book.setPrice(5000);
        book.setTitle("즐거운 자바");

        System.out.println(book.getPrice());
        System.out.println(book.getTitle());

        System.out.println("-------------");
        Book book2 = (Book) context.getBean("book1");
        if (book == book2) {
            System.out.println("같은 인스턴스");
        } else {
            System.out.println("다른 인스턴스");
        }
    }
}
```

---

# ApplicationContext

- ApplicationContext는 다양한 인터페이스를 상속받고 있다.
- 스프링 컨테이너의 핵심 인터페이스!

```
org.springframework.context
interface ApplicationContext
```

- 그 중에서도 BeanFactory도 ApplicationContext를 상속받는다.
```
org.springframework.beans.factory
Interface BeanFactory
```

---

# ApplicationContext를 구현하고 있는 대표적인 클래스

- CLASSPATH에서 XML설정 파일을 읽어들여 동작한다.
```
org.springframework.context.support
Class ClasspathXmlApplicationContext
```

---

## 스프링 프레임워크 핵심 모듈

- Core Container 부분이 가장 핵심
- Gradle에서 아래의 라이브러리를 추가한다.

implementation group: 'org.springframework', name: 'spring-context', version: '5.3.23'

![height:450](img.png)

---

## Spring ApplicationContext를 사용해보자

```
package com.example.spring02;

import exam.Book;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringApplicationContextExam {
    public static void main(String[] args) {
        // 인스턴스를 인터페이스 타입으로 참조할 수 있다.
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        Book book1 = (Book) context.getBean("book1");
        Book book1 = context.getBean("book1",Book.class);
        book1.setTitle("즐거운 Spring Boot");
        book1.setPrice(5000);

        System.out.println(book1.getTitle());
        System.out.println(book1.getPrice());

        Book book2 = (Book)context.getBean("book1");
        System.out.println(book2.getPrice());
    }
}
```

# MyService & MyDao 클래스다이어그램

- 연관관계
  - MyService가 MyDao를 가진다.

![height:200](serviceDAO.png)

---

# MyService & MyDao

- 프로그래머가 직접 인스턴스를 생성하고 주입하는 방법
- setter주입
```
  MyService myService = new MyService();
  MyDao myDao = new MyDao();
  myService.setMyDao(myDao);
```
- 생성자에 주입
```
  MyService myService = new MyService(new MyDao());
```

---

# Spring 설정으로 주입

```
  MyService myService = new MyService();
  MyDao myDao = new MyDao();
  myService.setMyDao(myDao);
```

```
   <bean id="myService" class="com.example.spring02.component.MyService">
      <property name="myDao" ref="myDao"></property>
   </bean>
   <bean id="myDao" class="com.example.spring02.component.MyDao"></bean>
```

---

# Annotation을 이용한 컨테이너

- Spring 3.0부터 등장
- Annotation기반 (Java Config, Component Scan)

```
org.springframework.context.annotation
Class AnnotationcConfigApplicationContext
```

---

## AnnotationConfigApplicationContext

```
package com.example.spring02.config;

import com.example.spring02.component.MyDao;
import com.example.spring02.component.MyService;
import exam.Book;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Java Config 설정을 AnnotationConfigApplicationContext로 읽어들인다.
// ApplicationConfig에 대한 인스턴스를 만든다.
@Configuration
public class ApplicationConfig {
    public ApplicationConfig(){
        System.out.println("ApplicationConfig()");
    }

    // <bean id="book1" class="exam.Book"></bean>
    // 메소드 이름 : id
    @Bean
    public Book book1(){
        return new Book();
    }

    /*
    <bean id="book2" class="exam.Book">
        <property name="title" value="즐거운 자바"></property>
        <property name="price" value="5000"></property>
    </bean>
    */
    @Bean
    public Book book2(){
        Book book = new Book();
        book.setTitle("즐거운 자바.");
        book.setPrice(9000);
        return book;
    }

    /*
    <bean id="myService" class="com.example.spring02.component.MyService">
        <property name="myDao" ref="myDao"></property>
    </bean>
    */
    @Bean(name = "myService2") // bean id가 myService2
    public MyService myService(MyDao myDao){
        MyService myService = new MyService();
        myService.setMyDao(myDao);
        return myService;
    }

    // <bean id="myDao" class="com.example.spring02.component.MyDao"></bean>
    @Bean
    public MyDao myDao(){
        return new MyDao();
    }
    
//    컴포넌트 스캔으로 Bean을 만들어준다.
//    @Bean
//    public MyUtil myUtil(){
//        return new MyUtil();
//    }
}
```

---

## Component Scan

```
package com.example.spring02.component;

import org.springframework.stereotype.Component;

// Component Scan방식.
// AnnotationConfigApplicationContext의 @Component를 찾아서 인스턴스를 생성한다.(config에서 bean설정을 안해줘도 된다.)
// value에 있는 값이 id가 된다.
@Component(value = "myUtil2")
public class MyUtil {
    public MyUtil() {
        System.out.println("MyUtil()");
    }

    public void print() {
        System.out.println("MyUtil.print()");
    }
}
```

## Web Application

- 웹 애플리케이션(web application) 또는 웹 앱은 소프트웨어 공학적 관점에서 인터넷이나 인트라넷을 통해 웹 브라우저에서 이용할 수 있는 응용 소프트웨어를 말한다.

---

## Web Application Server

- 웹 애플리케이션 서버(Web Application Server, 약자 WAS)는 웹 애플리케이션과 서버 환경을 만들어 동작시키는 기능을 제공하는 소프트웨어 프레임워크이다. 인터넷 상에서 HTTP를 통해 사용자 컴퓨터나 장치에 애플리케이션을 수행해 주는 미들웨어(소프트웨어 엔진)로 볼 수 있다. 웹 애플리케이션 서버는 동적 서버 콘텐츠를 수행하는 것으로 일반적인 웹 서버와 구별이 되며, 주로 데이터베이스 서버와 같이 수행이 된다. 한국에서는 일반적으로 "WAS" 또는 "WAS S/W"로 통칭하고 있으며 공공기관에서는 "웹 응용 서버"로 사용되고, 영어권에서는 "Application Server"(약자 AS)로 불린다.

- 자바 EE 표준준수 웹 애플리케이션 서버
  - 스프링, 스프링 부트를 사용하는 사용자는 이것을 WAS라고 한다.

---

## Java EE에 대한 표준을 일부 준수

- 아파치 톰캣(Apache Tomcat): 오픈 소스 재단 아파치 소프트웨어 재단의 오픈 소스 소프트웨어

---

## Java EE Platform Specification

- https://javaee.github.io/javaee-spec/

---

## Java EE에서 Jakarta EE로의 전환

- https://www.samsungsds.com/kr/insights/java_jakarta.html

---

## Java 웹 프로그래밍

- Java언어로 웹 어플리케이션을 만들겠다.
- 자바 웹 어플리케이션이 실행될 수 있는 WAS가 필요하다.
  - iPhone앱은 iOS위에서 동작한다.
- Servlet/JSP를 실행할 수 있는 환경(Servlet 컨테이너)
  - JSP도 알고보면 Servlet기술로 만들어진다.
  - Servlet 컨테이너는 WAS안에 있다.
  - WAS는 여러개의 웹 어플리케이션을 실행할 수 있다.
  - 대표적인 WAS는 Tomcat

---

## Tomcat ?

- https://tomcat.apache.org/
  - apache-tomcat-x.x.xx.tar.gz 을 다운 받는다
  - 특정 폴더에 복사한 후 압축을 해제
    - tar xvfz apache-tomcat-x.x.xx.tar.gz
  - apache-tomcat-x.x.xx/bin
    - startup.sh을 실행하면 서버가 실행된다.
      - 윈도우는 startup.bat파일을 실행한다.
    - shutdown.sh을 실행하면 서버가 종료된다.
      - 윈도우는 shutdonw.bat를 실행한다.

---

## startup.sh을 수정한 후 실행한다.

- 마지막 줄
```
exec "$PRGDIR"/"$EXECUTABLE" start "$@"
```

- 다음과 같이 수정한다.
```
exec "$PRGDIR"/"$EXECUTABLE" run "$@"
```

- 백그라운드가 아닌 포그라운드로 실행된다.
 
---

## Tomcat이 기본으로 제공하는 Web Application

apache-tomcat-x.x.xx/webapps

```
drwxr-x---@ ROOT/
drwxr-x---@ docs/
drwxr-x---@ examples/
drwxr-x---@ host-manager/
drwxr-x---@ manager/
```

---

## Tomcat이 성공적으로 실행되었다면

- http://localhost:8080/
  - ROOT 웹 어플리케이션

- http://localhost:8080/docs
  - docs 웹 어플리케이션

- http://localhost:8080/examples
  - examples 웹 어플리케이션

---

## examples 폴더의 구조

```
drwxr-xr-x ./
drwxr-xr-x ../
drwxr-xr-x META-INF/
drwxr-xr-x WEB-INF/
-rw-r--r-- index.html
drwxr-xr-x jsp/
drwxr-xr-x servlets/
drwxr-xr-x websocket/
```

## Tomcat을 이용한 웹 어플리케이션을 만든다는 의미는?

http://localhost:8080/

    - 내가 만든 사이트가 보여지려면?
    - webapps/ROOT 의 내용을 내가 만든 내용으로 바꾸면 된다.
    - Tomcat서버에 내가 만든 웹 애플리케이션을 deploy한다.

---

## 서버와 브라우저의 동작

---

## 웹 어플리케이션을 어떻게 만들까?
    - Eclipse IDE, IntelliJ IDE는 손쉽게 가능하게 해준다.

---

## IntelliJ 에서 웹 어플리케이션 만들기

- 프로젝트 이름 : servletexam
- 실행 :
    http://localhost:8080/servletexam_war_exploded/

- 만든 웹 애플리케이션이 ROOT 웹 애플리케이션이 아니다.
- src/main/webapp/index.jsp 가 보여진 것

---

## http://localhost:8080/ 로 실행하려면?

- edit configuration(Tomcat)
  - Deployment
    - Application Context(/)

---

## 브라우저 개발자도구

- 네트워크 보기(F12)

---

## http://localhost:8080

- http://localhost:8080/

1) 브라우저는 서버에 접속
2) 요청 정보를 보낸다
```
GET /
헤더들
빈줄
```
    - GET : 요청메소드

3) Tomcat는 / 에 해당하는 요청은 index.jsp를 읽어들여 응답

```
200 OK
응답헤더들
빈줄
index.jsp의 내용
```

---

## 서블릿이란?

- HttpServlet을 상속받는 클래스를 말한다.
- JSP도 특수한 형태의 서블릿이라고 말할 수 있다.

---

## 서블릿을 실행

http://localhost:8080/hello-servlet

```
@WebServlet(name = "helloServlet", value = "/hello-servlet")
```

---

## Tomcat은?

/hello-servlet 에 해당하는 서블릿을 찾아서 실행.

HelloServlet을 실행

GET방식으로 요청했기 때문에 HelloServlet의 doGet실행.

---

## IntelliJ로 프로젝트를 생성하면 자동으로 만들어주는 Servlet이 있다.

```
package com.example.servletexam;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {    
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
    }
}
```

---

## HttpServlet의 동작 원리.

- Tomcat이 요청을 받아서, 어떤 서블릿을 실행하는가?
- Tomcat은 요청정보를 HttpServletRequest, 응답을 위해 HttpServletResponse 인스턴스를 생성
- Tomcat은 서블릿의 doGet메소드의 인자로 위의 객체를 전달하여 실행해달라고 한다.

- Servlet 코드

```
- 요청정보로부터 브라우저가 보내주는 값을 읽어들인다.
- 원하는 코드를 작성
- 그 결과를 응답에게 써준다. (PrintWriter)
```

---

## 스프링 웹 프로그래밍

- 가장 핵심이 되는 클래스
    DispatcherServlet

---

## JSP

- 특수한 형태의 서블릿
- MS ASP 스크립트 기술

- http://localhost:8080/index.jsp

- WAS는 다음과 같은 작업을 실행한다.
  - 처음 실행되면
    - index.jsp가 실행되면 index_jsp.java 파일이 생성된다.
    - 문법오류가 없으면 자동으로 index_jsp.class가 생성된다.
    - 이 파일이 실행된다.
    - _jspInit()
  - 두번째 실행부터
    - _jspService()
---

##JSP

- org.apache.jasper.runtime.HttpJspBase 를 상속받는다.
- HttpJspBase는 HttpServlet과 같은 역할
- Servlet init() - _jspInit()
- Servlet destroy() - _jspDestroy()
- Servlet service(req, res) - _jspService(req, res)

---

## JSP

- _jspService메소드에 다음과 같은 변수들이 선언되어 있다.
  - JSP 내장 변수

```
  메소드의 매개변수 타입 :
    final javax.servlet.http.HttpServletRequest req,
    final javax.servlet.http.HttpServletResponse res
  final javax.servlet.jsp.PageContext pageContext;
  javax.servlet.http.HttpSession session = null;
  final javax.servlet.jsp.ServletContext application;
  final javax.servlet.jsp.ServletConfig config;
  javax.servlet.jsp.JspWriter out = null;
  final java.lang.Object page = this;
  javax.servlet.jsp.JspWriter _jspx_out = null;
  javax.servlet.jsp.PageContext _jspx_page_context = null;
```

---

## Servlet vs JSP

- Servlet은 java소스
  - java코드를 넣어서 개발한다.
  - java코드로 작성하는 비지니스 로직
  - HTML, CSS 입력이 어렵다.

- JSP (사용하지 말자!) ==> 다른 템플릿 기술.
  - HTML + CSS : 프론트개발자, 퍼블리셔
  - java코드를 넣을 수 있다. (되도록 자바코드는 넣지 않는다.)

---