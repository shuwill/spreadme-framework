<h1 align="center">Spreadme Framework</h1>

<p align="center">
    <a href="#Apahce"><img src="https://img.shields.io/badge/License-Apache-brightgreen.svg" alt="Apache"></a>
    <a href="#Maven"><img src="https://img.shields.io/badge/Build-Maven-blue.svg" alt="Maven"></a>
    <a href="#Java"><img src="https://img.shields.io/badge/Programme-Java-important.svg" alt="Java"></a>
</p>

Directory
------
- [Directory](#directory)
- [Liense](#liense)
- [Spreadme Commons](#spreadme-commons)
- [Spreadme Boot](#spreadme-boot)
- [Spreadme Data](#spreadme-data)
- [Spring Component](#spring-component)

Liense
------
This code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0).

Spreadme Commons
------

- Install

  - #### Maven
  - #### Java8 +
  - #### Spread-commons
    ``` xml
    <dependency>
        <groupId>org.spreadme</groupId>
        <artifactId>spreadme-commons</artifactId>
        <version>1.5.2</version>
    </dependency>
    ```

- Example
  - Date Util
    ```java
    // 线程安全的时间解析工具
    String formatter = "HH:mm:ss dd.MM.yyyy";
    String text = "19:30:55 03.05.2015";
    Date data = Dates.parse(text, formatter);
    // Sun May 03 19:30:55 CST 2015

    // 格式化日期
    Dates.format(new Date(), formatter)
    // 11:00:40 03.04.2020

    // 其他用法
    Console.info("今天开始时间: %s", Dates.getStartOfDate(new Date()));
    Console.info("今天结束时间: %s", Dates.getEndOfDate(new Date()));
    Console.info("100天前的时间: %s", Dates.getDate(new Date(), ChronoUnit.DAYS, -100));
    Console.info("时间戳: %s", Dates.getTimestamp());
    Date oldDate = Dates.parse("19:30:55 03.05.2015", formatter);
    Duration duration = Dates.getDuration(new Date(), oldDate);
    Console.info("相差天数: %d", duration.toDays());
    ```

  - IO Util
    ```java
    IOUtil.copy(InputStream, OutputStream);
    RepeatableInputStream in = IOUtil.toRepeatable(InputStream inputStream);
    IOUtil.zipFiles(List<File> files, OutputStream out);
    IOUtil.zipResouces(final List<Resource> entries, OutputStream out)
    ...
    ```

  - 验证码工具 Captcha
    ```java
    CaptchaCode code = LineCaptcha.of(200, 50).create();
    ```
    <img src="https://spreadme.oss-cn-shanghai.aliyuncs.com/static/img/captcha.png" alt="Apache"></a>
    ```java
    CaptchaCode code = CurvesCaptcha.of(200, 50).create();
    ```
    <img src="https://spreadme.oss-cn-shanghai.aliyuncs.com/static/img/CurvesCaptcha.png" alt="Apache"></a>

  - Reflect 反射工具
    ```java
    private final String java_source_file = "CompilePerson.java";
    private final String class_name = "org.spreadme.commons.test.CompilePerson";
    try (FileInputStream in = new FileInputStream(java_source_file)) {
	    final String content = StringUtil.fromInputStream(in);
	    Reflect.compile(class_name, content).create("Tom", 27)
		    .invoke("hello")
		    .set("name", "Jack").set("age", 28)
		    .fields()
		    .forEach((key, value) -> Console.info("field name %s, value %s", key, value.get()));
    }

    //INFO[2020-04-04 19:17:23] My name is Tom, age is 27, randome string is RUEFkyHI
    //INFO[2020-04-04 19:17:23] field name name, value Jack
    //INFO[2020-04-04 19:17:23] field name age, value 28
    ```

  - Codec
    ```java
    //Hex
    String plainText = StringUtil.randomString(10);
    String hex = Hex.toHexString(plainText.getBytes(Charsets.UTF_8));
    Hex.decode(hex);
    //Base64
    String base64 = Base64.toBase64String(plainText.getBytes(Charsets.UTF_8));
    Base64.decode(base64);
    ```

  - Digest
    ```java
    FileInputStream in = new FileInputStream(testFile)
    byte[] md5 = Digest.get(in, Algorithm.MD5);
    byte[] sha256 = Digest.get(in, Algorithm.SHA256);
    ```

Spreadme Boot
------
a simple autoconfigure project by springboot

usage reference springboot autoconfigure


Spreadme Data
------
Mybatis autoconfiguration by springboot

- Example
  ```java
  @Entity
  @Table(name = "USERS")
  public class User extends IdEntity {

  }

  public interface UserDao extends BaseDao<User>, Mapper {

	  String getWaterMarkKey(Long id);

  }

  @Service
  @Transactional
  public class UserServiceImpl extends BaseCrudService<User> implements UserService {

	  @Autowired
	  private UserDao userDao;

	  @Override
	  public User get(Long id) {
		  return super.findById(id).orElse(null);
	  }

	  @Override
	  public String getWaterMarkKey(Long id) {
		  return userDao.getWaterMarkKey(id);
	  }

  }
  ```

Spring Component
------
distributed cache,message,job 

usage reference test case