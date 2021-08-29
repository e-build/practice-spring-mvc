***목차***
- [개요](#개요)
- [main() 메서드와 메서드 스택 프레임](#main()-메서드와-메서드-스택-프레임)
- [변수와 메모리](#변수와-메모리)
    + [지역 변수와 메모리](#지역-변수와-메모리)
    + [블록 구문과 메모리](#블록-구문과-메모리)
    + [전역 변수와 메모리](#전역-변수와-메모리)
- [메서드와 메모리](#메서드와-메모리)
- [인스턴스와 메모리](#인스턴스와-메모리)
- [멀티 스레드와 멀티 프로세스의 이해](#멀티-스레드와-멀티-프로세스의-메모리-사용)

### 학습목표
> 1. 자바에서 지역변수, 전역변수, 멤버변수가 메모리의 어느 영역에 어떻게 저장되고 소멸되는지 이해한다. 
> 2. 메서드 호출에 따른 메모리의 변화를 이해한다.


# 개요 
자바 프로그램이 어떻게 개발되고 구동되는 지를 알기 위해서는 JVM(Java Virual Machine)의 존재와 역할을 아는 것이 필수적이다. 먼저 "자바라는 언어를 활용하여 프로그램을 개발한다는 것"은 JDK(Java Development Kit)라고 하는 JVM용 소프트웨어 개발도구를 활용하여 프로그래밍하는 것을 의미한다. JVM은 이름 그대로 가상기계이며, 자바프로그램은 JVM 이라고 하는 가상의 컴퓨터 위에서 실행되는 JRE(Java Runtime Environment) 라는 JVM용 운영체제를 통해 실행되는 것이다.

이와 같은 연관관계를 고려했을 때, JVM, JRE, JDK 라고 하는 각각의 배포판들 끼리의 버전 충돌을 피하고 통합적으로 관리되어야 하는 편의를 위해, 일반적으로 JDK가 JRE를 포함하고 다시 JRE는 JVM을 포함하는 형태로 배포된다.

자바가 메모리를 어떻게 활용하는 지에 대한 학습 이전에 프로그래밍 영역의 모든 프로그램들이 메모리를 사용하는 공통된 방식에 대하여 알아보자.
일반적으로 프로그램은 "코드를 실행하는 영역"과 "데이터를 저장하는 영역"으로 구분하여 메모리를 사용한다. 운영체제나 언어 자체를 개발하는 로우 레벨 개발자가 아닌 이상 애플리케이션 영역의 개발자가 코드 실행영역을 깊게 학습할 필요는 없다. 그래서 데이터 저장 영역의 삼분할 구조만 살펴볼 것이다. 데이터 저장 영역은 다시 다음과 같이 세 가지 영역으로 구분할 수 있다.

- 스태틱(static) 영역 : 클래스들의 놀이터
- 스택(stack) 영역 : 메서드들의 놀이터
- 힙(heap) 영역 : 객체들의 놀이터
<table>
    <tr>
        <th colspan="2">STATIC 영역</th>
    </tr>
    <tr>
        <th>STACK 영역</th>
        <th>HEAP 영역</th>
    </tr>
</table>

> 위와 같이 데이터 저장 영역을 삼분할한 메모리 구조를 `T메모리`라고 부를 것이다.

# main() 메서드와 메서드 스택 프레임

main() 메서드는 프로그램이 실행되는 시작점이다. main() 메서드가 실행될 때 메모리에서 발생하는 일들을 대략적인 순서로 정리하면 다음과 같다.

1. JRE는 먼저 프로그램 안에 main() 메서드가 있는 지 확인한다.
2. main()메서드의 존재가 확인되면 JRE는 프로그램 실행을 위해 JVM을 부팅한다.
3. 부팅된 JVM은 목적파일을 받아 그 목적파일을 실행한다.
    - 목적파일을 실행하기 이전에 JVM은 다음과 같은 전처리 과정을 수행한다. 
      1. 우선 java.lang 패키지를 메모리의 스태틱 영역에 로딩시켜 놓는다. 그 다음 
      2. 개발자가 작성한 모든 클래스와 import 패키지를 스태틱 영역에 가져다 놓는다.
    > ***목적파일이란?***
   > *  소스 코드가 컴파일 되어 생성되는 파일이다. 자바 프로그래밍에서는 일반적으로 `.java` 파일에 소스코드를 작성하고 
   > 자바 컴파일러를 통해 컴파일된 파일은 `.class` 파일이 된다.
4. main() 메서드가 실행될 스택 프레임(stack frame)이 스택 영역에 할당된다.
    - 정확히 이야기하면, 여는 중괄호 "{" 를 만날 때마다 스택 프레임이 하나씩 생긴다.(클래스 수준의 중괄호 제외)
    - 이후 닫는 중괄호 "}"를 만나면 스택 프레임이 소멸된다.
5. 메서드의 인자 args를 저장할 변수공간을 스택 프레임의 맨 밑에 확보한다.
6. main() 메서드 안의 명령문을 차례대로 수행한다.
7. main() 메서드는 프로그램의 시작점이자 끝이기도 하기 때문에, main() 메서드가 끝나면 JRE는 JVM을 종료하고 JRE 자체도 운영체제 상의 메모리에서 사라진다.

# 변수와 메모리
자바에서 변수를 할당함에 따라 메모리의 static, stack, heap 영역에 어떤 변화가 생기는 지 알아본다.
### 지역 변수와 메모리
흔히 지역변수라고 하는 것은 메서드내에서 혹은 중괄호로 둘러싼 블럭 내에서 선언된 변수를 의미한다. 반드시 변수의 범위가 지역적으로 정해져 있기 때문에, 그 범위를 넘어가서는 사용될 수 없다. 
```java
public class MemoryCheck {
    public static void main(String[] args){
        int var1;
        var1 = 10;
        
        double var2 = 2.0;
    }
}
```
위와 같은 main() 메서드 내에서 변수가 선언되고 값이 할당됨에 따라 메모리에 나타나는 변화는 다음과 같다.
1. var1 선언
   * 메모리의 스택영역에 할당된 main()메서드의 스택프레임에 4바이트 크기의 정수 저장공간이 마련된다.
    * 마련된 저장공간에는 어떤값이 들어있는 지 알 수 없다. 아직 초기화되지 않았기 때문에 이전에 해당 공간의 메모리를 사용했던 다른 프로그램이 청소하지 않고 간 값을 그대로 가지고 있을 수도 있다.
2. var1에 값 할당
    * 값을 할당하는 순간 var1의 메모리의 공간에 10이 저장된다.
3. var2 선언 및 할당
    * double 타입에 크기(8바이트)에 맞는 메모리공간이 생성되고, 2.0이 저장된다.
4. main() 메서드가 종료되는 } 를 만났을 때, main() 메서드의 스택프레임이 스택영역에서 사라진다. 

즉, 지역변수에 대한 설명과 함께 언급했던 것처럼 변수의 범위가 지역적으로 정해져 있다는 것은, 스택영역 내에 특정 스택프레임안의 메모리공간이 할당되었다는 것이고, 이는 스택영역내에서 서로 다른 스택프레임의 
메모리에 접근할 수 없다는 것을 의미한다.
### 블록 구문과 메모리
```java
public class MemoryCheck {
    public static void main(String[] args){
        int var1 = 10;
        int var2 = 20;
        
        if(var1 == 10){
            int v_1 = var2 + 5;
            var2 = v_1;
        } else {
            int v_2 = var2 + 5;
            var2 = v_2;
        }
    }
}
```
여는 중괄호를 만나면 스택프레임이 시작된다고 했었다. 따라서, 위와 같은 경우 스택프레임의 생성 및 소멸 순서를 고려해보면 다음과 같다.
1. main() 메서드 스택프레임 생성
2. main() 메서드 스택프레임 내에 var1, var2 지역변수 메모리 공간 생성
3. var1==10 이 true 이기 때문에 main()메서드 스택프레임 내에서 if 블록의 스택프레임이 중첩되어 생성.
4. if 블록의 스택프레임 내의 v_1 의 지역변수 메모리 공간 생성
5. if 블록의 스택프레임에서 상위 스택프레임에 접근하여 var2 에 값 할당
6. if문 종료와 함께 스택프레임 소멸
7. main() 메서드 스택프레임 소멸

위와 같은 과정을 통해서 우리는 세 가지 사실을 알 수 있다. 첫 번째로 아예 실행되지 않은 else문은 스택 프레임이 생성되지 않았다. 두 번째는 스택프레임의 소멸과 함께 지역변수를 위한 메모리공간도 소멸된다는 것이다.
마지막으로 세번째는 중첩된 내부 스택프레임의 변수에서 외부 스택프레임의 변수에 접근하는 것은 가능하나 그 역은 불가능하다는 것이다.

### 전역 변수와 메모리
두 메서드 사이에 값을 전달하는 방법은 메서드를 호출할 때 메서드의 인자를 이용하는 방법과 메서드를 종료할 때 반환값을 넘겨주는 방법이 있다. 
이와 같이 메서드의 서명(인자, 반환값)을 이용하지 않고도 메서드간에 값을 공유하는 방법은 전역변수를 이용하는 것 뿐이다.
```java
public class MemoryCheck {

    static int globalVar1;

    public static void main(String[] args){
        globalVar1 = 10;
        
        int temp = foo(6, 3);
        
        System.out.println(globalVar1);
        System.out.println(temp);
    }
    
    public static int foo(int a, int b){
        globalVar1 = a + b;
        return a - b;
    }
}
```
코드를 보면 share 변수에 static 키워드가 붙어있다. 그래서 share 변수는 T 메모리의 스태틱 영역에 변수 공간이 할당된다. static 키워드의 의미를 간단히 살펴보면 다음과 같다.

그렇다면, 위 코드의 실행에 따른 메모리의 변화를 살펴보자 
1. MemoryCheck 클래스가 T 메모리의 스태틱 영역에 배치될 때 그 안에 share 변수가 클래스의 멤버로 공간을 만들어 저장된다.
2. main() 메서드의 스택프레임이 생성된다.
3. globalVar1 에 10을 할당한다. 
3. 지역변수 temp를 위한 메모리 공간이 스택프레임내에 생성된다,
4. temp에 값을 할당하는 foo() 메서드의 스택프레임이 생성되고 제어권이 넘어간다. 
5. foo()에서 globalVar1에 값을 할당하면 globalVar1의 메모리에 접근하여 변수의 값을 변경시킨다.
6. 반환값이 temp에 할당되고, foo() 스택프레임이 소멸된다.
7. main() 메서드의 스택프레임이 소멸된다. 

지역변수와 전역변수의 선언과 할당에 따른 메모리 변화를 살펴보면 다음과 같은 해석이 가능하다.

| 스택 프레임에 종속적인 지역변수 <-> 스택 프레임에 독립적인 전역변수

전역변수는 여러 메서드에서 전역 변수의 값을 변경할 수 있는데, 이러한 점은 프로젝트 규모가 커짐에 따라 전역변수의 값을 추적하기 어렵게 만든다. 따라서 값을 수정할 수 없는 상수로 만들고 
오로지 읽기전용으로만 사용하는 것이 추천된다. 

# 메서드와 메모리
메서드를 호출할 때마다 해당 메서드의 스택 프레임이 생성된다. 만약 동일한 메서드가 재귀방식에 의해 여러번 호출된다면, 
호출된 순서대로 스택영역에 스택프레임이 쌓이게되고 다시 마지막에 호출된 메서드 부터 호출된 역순으로 스택프레임이 소멸되게 된다.   
> ***스택이란?*** 
> * 데이터 구조에서 흔히 들어보았을 **스택**이란 단어는 도대체 어떤 의미일까?
> * 자료구조는 크게 분류하여 선형 자료구조와 비선형 자료구조로 구분한다.  자료가 저장하고 출력하기 까지의 경로를 비유하여 
    대략 직선 형태라면 선형 자료구조이고 그렇지 않은 것은 비선형 자료구조이다.
> * 스택은 흔히 큐와 비교된다. 스택은 **가장 마지막에 들어온 데이터가 가장 먼저 출력되는 형태의 자료구조**이지만, 큐는 가장 먼저 들어온 데이터가 
> 가장먼저 출력되기 때문에 서로 상반되는 형태의 자료구조이기 때문이다. 

# 인스턴스와 메모리
다음과 같이 정의된 Car 클래스의 인스턴스가 생성되고 참조변수에 할당됨에 따라 메모리에 어떤 변화가 발생하는 지 알아보자.  
```java
class Car {
  public String modelName;
  public int currentPrice;
  public int type;
  
  public void go(){
    System.out.println(this.name +  " 이(가) 움직입니다.");
  }
}
public class CarMain{

  public static void main(String[] args){
    Car morning = new Car();
    morning.modelName = "morning";
    morning.currentPrice = "10000";
    morning.type = "LIGHT";
    morning.go();
    morning = null;
    
    Car sportage = new Car();
    sportage.modelName = "sportage";
    sportage.currentPrice = "20000";
    sportage.type = "SUV";
    sportage.go();
  
  }
}
```
CarMain 클래스의 main()메서드가 시작할 시점에 T 메모리는 다음과 같을 것이다.
* static 영역: java.lang 패키지, Car 클래스 로딩
    - Car 클래스의 속성들은 스태틱영역에서 아직 값을 가지고 있지 않는데, 이는 클래스의 멤버가 아니라 인스턴스 멤버이기 때문이다.
    - 클래스 멤버와 인스턴스 멤버는 static 키워드를 통해 구분한다.

morning 지역변수가 선언되고 객체를 할당하고, 이후에 null을 할당할 때 까지 T 메모리의 과정은 다음과 같다.
1. main() 메서드의 스택프레임에 morning 지역변수를 위한 메모리 공간이 생성된다.
2. new 연산자를 통해 새로운 Car 객체가 생성되고, 이 객체는 힙 영역에 메모리 공간이 생성된다.
3. 힙 영역에 존재하는 Car 객체의 메모리 주소값을 morning에 할당한다. 변수 morning이 새로 생성된 Car객체를 참조한다고 표현할 수 있다.
4. morning 에 . 이라는 참조 연산자를 사용해 실제 힙 영역에 있는 Car 객체에 접근하여, modelName, currentPrice, type에 값을 할당한다.
5. go() 를 실행하면 T 메모리 상의 변화는 없고, 코드 실행 영역에서 실행되어 콘솔에 "morning 이(가) 움직입니다." 가 출력된다.
6. morning 에 null 이 할당되면, 힙영역에 있는 Car 객체는 어느곳에서도 참조하지 않는 고아상태가 되고 이는 곧 JVM의 가비지 컬렉터가 수거한다.

다시 sportage 지역변수가 선언되고 새로운 Car 객체를 생성하여 할당하는 것을 볼 수 있는데, 이는 이전에 생성됐던 Car 객체가 아니라는 점을 알아야 한다.
따라서 인스턴스의 속성과 그에 따른 행위는 다르게 동작하게 되는 것이다. main() 메서드가 종료되면서 스택프레임이 소멸된다.

### 정적 멤버와 인스턴스 멤버
클래스 멤버, static 멤버, 정적 멤버 모두 다 같은 말이다. 또한 객체 멤버, 인스턴스 멤버, 오브젝트 멤버도 다 같은 말이다.
T 메모리의 스태틱 영역에 클래스가 배치될 때 클래스 속성과 인스턴스 속성의 동작이 다른데, 클래스 속성인 경우 클래스 내부에 메모리 공간이 확보된다.
이에 반해 인스턴스 속성은 속성명만 있고 실제 메모리 공간은 확보되지 않는다.
인스턴스 속성은 힙 영역에 객체가 생성되면 바로 그때 힙 영역내에 각각의 객체의 메모리 공간 안에 멤버 속성을 위한 메모리 공간이 할당된다.

### 상속 관계에서의 메모리 변화
상위 클래스인 Car와 하위클래스인 
```java
class Car {
    String modelName;
    int currentPrice;
    
    public void go(){
      System.out.println(this.name +  " 이(가) 움직입니다.");
    }
}

```
```java
class Sedan extends Car {
    int trunkSize;
  
    @Override
    public void go(){
      System.out.println(this.name +" 이(가) 공기의 저항을 강하게 받으며 움직입니다.");
    }
    
    public int printTrunkSize(){
      System.out.println(this.trunkSize);
    }

    public int printTrunkSize(int plusSize){
        this.trunkSize += 100;
        System.out.println(this.trunkSize);
    }
}
```
```java
class CarMain {
    
    public static void main(String[] args){
        Sedan sonata = new Sedan();
        sonata.modelName = "sonata";
        sonata.currentPrice = 10000;
        sonata.trunkSize = 500;

        sonata.printTrunkSize();
        sonata.go(); 

        Car grandeur = new Sedan();
        grandeur.modelName = "grandeur";
        grandeur.currentPrice = 10000;
        grandeur.trunkSize = 500;
    }
}
```
sonata와 grandeur라고 하는 객체를 만들었다. 두 객체 모두 상속받고 있는 Sedan 클래스의 인스턴스를 할당받기 때문에 힙메모리 상에서는
상위 클래스인 Car의 인스턴스와 하위 클래스인 Sedan의 인스턴스가 함께 생성된다. 상위클래스의 인스턴스도 함께 생성된다는 점을 통해 유추해보자면,
모든 클래스의 최상위 클래스인 Object 클래스의 인스턴스도 함께 생성될 것임을 알 수 있다. sonata와 grandeur의 차이는 sportage는 Sedan 타입이면서 Sedan 클래스의 인스턴스를 할당받지만,
grandeur는 Car 타입이면서 Sedan 클래스의 인스턴스를 할당받는다는 것이다. 이는 실제로 참조하는 힙메모리 상의 인스턴스가 다르다는 것인데,
Sedan 타입으로 선언된 sonata는 Sedan 클래스의 인스턴스를 참조하는 반면, Car 타입으로 선언된 grandeur는 Car 클래스의 인스턴스를 참조하는 것이다.

<b><i><u>자바에서 변수의 세가지 유형</u></i></b>

|이름|다른 이름|T 메모리 배치|
|--------------|---|---|
|정적 변수|클래스 [멤버]속성, 정적 변수, 정적 속성|스태틱 영역|
|인스턴스 변수|객체 [멤버]속성, 객체 변수, ...|힙 영역|
|지역 변수|지역 변수|스택 영역 (스택프레임 내부)|

# 멀티 스레드와 멀티 프로세스의 메모리 사용
멀티스레드는 T 메모리의 스택 영역을 스레드 개수만큼 분할해서 사용한다. 즉 하나의 T 메모리 안에서 스택 영역만 분할한 것이기 때문에 각 스레드들은 스태틱영역과 힙영역을 공유해서 사용할 수 있다.
여러 스레드들이 메모리를 공유해서 사용하는 만큼 여러 스레드에서 값을 읽고 쓰기 때문에 데이터의 무결성과 정합성에서 안전하지 특징이 있지만, 그만큼 메모리를 적게 사용할 수 있다.
반면, 멀티 프로세스는 다수의 데이터 저장 영역, 즉 다수의 T 메모리를 갖는 구조이다. 따라서 각 프로세스 각자의 T 메모리들은 고유공간이기 때문에 서로 참조할 수 없다. 
> 쓰기 가능한 전역변수를 사용하게 되면 스레드 안전성이 깨진다. 락(lock)을 걸어서 자원이 해제될때까지 대기하도록 할 수 있지만, 그만큼의 대기시간이 발생하기 때문에 
> 락을 거는 순간 멀티스레드의 장점은 버린 것과 같다.
```java
public class MemoryCheckThread extends Thread {

    static int globalVar1;

    public static void main(String[] args){
        MemoryCheckThread t1 = new MemoryCheckThread();
        MemoryCheckThread t2 = new MemoryCheckThread();
        
        t1.start();
        t2.start();
    }
    
    public void run(){
        for (int count=0; count < 10; count++) {
            System.out.println(globalVar1);
            
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace()
            }
        }
    }
}
```