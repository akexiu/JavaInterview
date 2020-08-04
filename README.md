# JavaInterview
面试相关
一、Java相关
Arraylist与LinkedList，HashMap默认空间是多少；
ArrayList会默认帮我们初始化数组的大小为10，
linkedList 是一个双向链表，没有初始化大小，也没有扩容的机制，就是一直在前面或者后面新增就好，对于双向链表的理解
HashMap 初始化大小是 16 ，扩容因子默认0.75（可以指定初始化大小，和扩容因子）扩容机制.(当前大小 和 当前容量 的比例超过了 扩容因子，就会扩容，扩容后大小为 一倍。例如：初始大小为 16 ，扩容因子 0.75 ，当容量为12的时候，比例已经是0.75 。触发扩容，扩容后的大小为 32.)

Arraylist与LinkedList区别与各自的优势List 和 Map 区别；
ArrayList实现了List接口,它是以数组的方式来实现的,数组的特性是可以使用索引的方式来快速定位对象的位置,因此对于快速的随机取得对象的需求,使用ArrayList实现执行效率上会比较好. 
LinkedList是采用链表的方式来实现List接口的,它本身有自己特定的方法，如: addFirst(),addLast(),getFirst(),removeFirst()等. 由于是采用链表实现的,因此在进行insert和remove动作时在效率上要比ArrayList要好得多!适合用来实现Stack(堆栈)与Queue(队列),前者先进后出，后者是先进先出.
解析: 因为ArrayList是使用数组实现的,若要从数组中删除或插入某一个对象，需要移动后段的数组元素，从而会重新调整索引顺序,调整索引顺序会消耗一定的时间，所以速度上就会比LinkedList要慢许多. 相反,LinkedList是使用链表实现的,若要从链表中删除或插入某一个对象,只需要改变前后对象的引用即可!
List：是存储单列数据的集合，存储的数据是有序并且是可以重复的
Map：存储双列数据的集合，通过键值对存储数据，存储 的数据是无序的，Key值不能重复，value值可以重复

谈谈HashMap，哈希表解决hash冲突的方法；
1.put()
HashMap put()方法源码如下：
public V put(K key, V value) {  
        if (key == null)  
            return putForNullKey(value);  
        int hash = hash(key.hashCode());  
        int i = indexFor(hash, table.length);  
        for (Entry<K,V> e = table[i]; e != null; e = e.next) {  
            Object k;  
            //判断当前确定的索引位置是否存在相同hashcode和相同key的元素，如果存在相同的hashcode和相同的key的元素，那么新值覆盖原来的旧值，并返回旧值。  
            //如果存在相同的hashcode，那么他们确定的索引位置就相同，这时判断他们的key是否相同，如果不相同，这时就是产生了hash冲突。  
            //Hash冲突后，那么HashMap的单个bucket里存储的不是一个 Entry，而是一个 Entry 链。  
            //系统只能必须按顺序遍历每个 Entry，直到找到想搜索的 Entry 为止——如果恰好要搜索的 Entry 位于该 Entry 链的最末端（该 Entry 是最早放入该 bucket 中），  
            //那系统必须循环到最后才能找到该元素。  
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {  
                V oldValue = e.value;  
                e.value = value;  
                return oldValue;  
            }  
        }  
        modCount++;  
        addEntry(hash, key, value, i);  
        return null;  
    }  
hash值冲突是发生在put()时，从源码可以看出，hash值是通过hash(key.hashCode())来获取的，当put的元素越来越多时，难免或出现不同的key产生相同的hash值问题，也即是hash冲突，
当拿到一个hash值，通过indexFor(hash, table.length)获取数组下标，先查询是否存在该hash值，若不存在，则直接以Entry<V,V>的方式存放在数组中，若存在，则再对比key是否相同,
若hash值和key都相同，则替换value，若hash值相同，key不相同，则形成一个单链表，将hash值相同，key不同的元素以Entry<V,V>的方式存放在链表中，这样就解决了hash冲突，这种方法叫做分离链表法，
与之类似的方法还有一种叫做 开放定址法，开放定址法师采用线性探测（从相同hash值开始，继续寻找下一个可用的槽位）hashMap是数组，长度虽然可以扩大，但用线性探测法去查询槽位查不到时怎么办？
因此hashMap采用了分离链表法。
2.get()
 public V get(Object key) {   
       if (key == null)   
           return getForNullKey();   
       int hash = hash(key.hashCode());   
       for (Entry<K,V> e = table[indexFor(hash, table.length)];   
           e != null;   
           e = e.next) {   
           Object k;   
           if (e.hash == hash && ((k = e.key) == key || key.equals(k)))   
                return e.value;   
        }   
        return null;   
    } 
有了上面存储时的hash算法作为基础，理解起来这段代码就很容易了。从上面的源代码中可以看出：从HashMap中get元素时，首先计算key的hashCode，找到数组中对应位置的某一元素，
然后通过key的equals方法在对应位置的链表中找到需要的元素。
当hashMap没出现hash冲突时，没有形成单向链表，get方法能够直接定位到元素，但是，出现冲突后，形成了单向链表，bucket里存放的不再是一个entry对象，而是一个entry对象链，
系统只能顺序的遍历每个entry直到找到想要搜索的entry为止，这时，问题就来了，如果恰好要搜索的entry位于该entry链的最末端，那循环必须要进行到最后一步才能找到元素，此时涉及到一个负载因子的概念，
hashMap默认的负载因子为0.75，这是考虑到存储空间和查询时间上成本的一个折中值，增大负载因子，可以减少hash表（就是那个entry数组）所占用的内空间，但会增加查询数据的时间开销，
而查询是最频繁的操作（put()和get()都用到查询）；减小负载因子，会提高查询时间，但会增加hash表所占的内存空间。
结合负载因子的定义公式可知，threshold就是在此loadFactor和capacity对应下允许的最大元素数目，超过这个数目就重新resize，以降低实际的负载因子。默认的的负载因子0.75是对空间和时间效率的一个平衡选择。
当容量超出此最大容量时， resize后的HashMap容量是容量的两倍：
3.hashMap数组扩容
当HashMap中的元素越来越多的时候，hash冲突的几率也就越来越高，因为数组的长度是固定的。所以为了提高查询的效率，就要对HashMap的数组进行扩容，数组扩容这个操作也会出现在ArrayList中，
这是一个常用的操作，而在HashMap数组扩容之后，最消耗性能的点就出现了：原数组中的数据必须重新计算其在新数组中的位置，并放进去，这就是resize。
那么HashMap什么时候进行扩容呢？当HashMap中的元素个数超过数组大小*loadFactor时，就会进行数组扩容，loadFactor的默认值为0.75，这是一个折中的取值。也就是说，默认情况下，数组大小为16，
那么当HashMap中元素个数超过16*0.75=12的时候，就把数组的大小扩展为 2*16=32，即扩大一倍，然后重新计算每个元素在数组中的位置，扩容是需要进行数组复制的，复制数组是非常消耗性能的操作，
所以如果我们已经预知HashMap中元素的个数，那么预设元素的个数能够有效的提高HashMap的性能。
 
为什么要重写hashcode()和equals()以及他们之间的区别与关系；
为何要重写hashCode()和equals()两个方法：
我们在写一些项目时，经常会希望两个不同对象的某些属性值相同时、就认为他们两个对象相同，但是重写equals()之前他们是不同的地址值的，所以我们要重写equals()方法，按照原则，我们重写了equals()方法，也要重写hashCode()方法，要保证这么三个原则（a. 在java应用程序运行时，无论何时多次调用同一个对象时的hsahCode()方法，这个对象的hashCode()方法的返回值必须是相同的一个int值。b. 如果两个对象equals()返回值为true,则他们的hashCode()也必须返回相同的int值。c. 如果两个对象equals()返回值为false,则他们的hashCode()返回值也必须不同）;所以java中的很多类都重写了这两个方法,例如String类，以及基本类型的包装类(引用类型)等。
在什么使用场景下要重写hashCode()和equals()两个方法：
当我们有自定义的一个class，想要把它的一个或多个实例保存在集合中时，我们就需要重写这两个方法。
比如HashSet存放元素时，根据元素的hashCode值快速找到要存储的位置，如果这个位置有元素，两个对象通过equals()比较，如果返回值为true,则不放入；如果返回值为false,则这个时候会以链表的形式在同一个位置上存放两个元素，这会使得HashSet的性能降低，因为不能快速定位了。还有一种情况就是两个对象的hashCode()返回值不同，但是equals()返回true,这个时候HashSet会把这两个对象都存进去，这就和Set集合不重复的规则相悖了;所以，我们重写了equals()方法时，也要按照重写hashCode()方法！
如何重写hashCode()和equals()两个方法：
在你的这个class中，有一系列变量和它们的getter和setter方法，在这类里重写@Override hashCode()和equals()即可。
区别与关系：
equals()是对象类object的基础方法,可以被重写,如果不重写,意义和==没有区别(string类默认重写了equals方法和hashCode方法).
hashCode()生成的是散列值,在散列表中有用,散列表通过散列值可以快速的定位数据位置,支持散列表的集合像HashMap.HashSet等.
原则
1.同一个对象（没有发生过修改）无论何时调用hashCode()得到的返回值必须一样。
如果一个key对象在put的时候调用hashCode()决定了存放的位置，而在get的时候调用hashCode()得到了不一样的返回值，这个值映射到了一个和原来不一样的地方，那么肯定就找不到原来那个键值对了。
2.hashCode()的返回值相等的对象不一定相等，通过hashCode()和equals()必须能唯一确定一个对象。不相等的对象的hashCode()的结果可以相等。hashCode()在注意关注碰撞问题的时候，也要关注生成速度问题，
完美hash不现实。
3.一旦重写了equals()函数（重写equals的时候还要注意要满足自反性、对称性、传递性、一致性），就必须重写hashCode()函数。而且hashCode()的生成哈希值的依据应该是equals()中用来比较是否相等的字段。

Object的hashcode()是怎么计算的？

若hashcode方法永远返回1或者一个常量会产生什么结果？

Java Collections和Arrays的sort方法默认的排序方法是什么；
一、Arrays.sort()
基本类型：采用调优的快速排序；
对象类型：采用改进的归并排序。
1、基本类型
数组长度小于47的时候是用直接插入算法，大于47并且小于286是采用双轴快速排序，大于286如果连续性好「也就是元素大多有序，有一个flag专门用来记录数组元素的升降次数，代表这个数组的连续性」采用的是归并排序，否则还是依旧采用双轴快速排序。
小于286（小于47/大于47并且小于286）
private static void sort(int[] a, int left, int right, boolean leftmost) {
        int length = right - left + 1;

        // 长度小于47，使用直接插入排序
        if (length < INSERTION_SORT_THRESHOLD) {
            if (leftmost) {
                /*
                 * Traditional (without sentinel) insertion sort,
                 * optimized for server VM, is used in case of
                 * the leftmost part.
                 */
                for (int i = left, j = i; i < right; j = ++i) {
                    int ai = a[i + 1];
                    while (ai < a[j]) {
                        a[j + 1] = a[j];
                        if (j-- == left) {
                            break;
                        }
                    }
                    a[j + 1] = ai;
                }
            }
            //长度大于47小于286
            else {
                //双轴快速排序
            }
大于等于286
static void sort(int[] a, int left, int right,
                     int[] work, int workBase, int workLen) {
        // 小于286排序
        if (right - left < QUICKSORT_THRESHOLD) {
            sort(a, left, right, true);
            return;
        }

        /*
         * Index run[i] is the start of i-th run
         * (ascending or descending sequence).
         */
        int[] run = new int[MAX_RUN_COUNT + 1];
        int count = 0; run[0] = left;

        //检查数组是否接近排好序
        for (int k = left; k < right; run[count] = k) {
            if (a[k] < a[k + 1]) { // 上升
                while (++k <= right && a[k - 1] <= a[k]);
            } else if (a[k] > a[k + 1]) { // 下降
                while (++k <= right && a[k - 1] >= a[k]);
                for (int lo = run[count] - 1, hi = k; ++lo < --hi; ) {
                    int t = a[lo]; a[lo] = a[hi]; a[hi] = t;
                }
            } else { // 相等
                for (int m = MAX_RUN_LENGTH; ++k <= right && a[k - 1] == a[k]; ) {
                    if (--m == 0) {
                        sort(a, left, right, true);
                        return;
                    }
                }
            }

            /*
             *连续性不好,
             *依然使用快速排序.
             */
            if (++count == MAX_RUN_COUNT) {
                sort(a, left, right, true);
                return;
            }
        }

        //检查特殊情况
        // Implementation note: variable "right" is increased by 1.
        if (run[count] == right++) { // The last run contains one element
            run[++count] = right;
        } else if (count == 1) { // The array is already sorted
            return;
        }

    		 /*
             *连续性好,
             *使用归并排序.
             */
    		.............
        }
    }

2、对象类型
TimSort.sort()
 public static void sort(Object[] a) {
        if (LegacyMergeSort.userRequested)//如果设置了归并排序为true
            legacyMergeSort(a);
        else//否则使用TimeSort(结合了归并排序和插入排序)
            ComparableTimSort.sort(a, 0, a.length, null, 0, 0);
    }
当待排序元素小于32个时，采用二分插入排序，是插入排序的一种改进。
当待排序元素大于等于32个时，进行归并排序。
二、Collectiom.sort()
 @SuppressWarnings({"unchecked", "rawtypes"})
    default void sort(Comparator<? super E> c) {
        Object[] a = this.toArray();
        Arrays.sort(a, (Comparator) c);
        ListIterator<E> i = this.listIterator();
        for (Object e : a) {
            i.next();
            i.set((E) e);
        }
    }
使用的是Arrays.sort()中的TimSort.sort()
Arrays.sort()分为基本数据类型和对象类型。
Collections.sort()是先转换为数组，再调用Arrays.sort()，也就是对象类型。

引用计数法与GC Root可达性分析法区别；

浅拷贝和深拷贝的区别；
String s="abc"和String s=new String("abc")区别；
HashSet方法里面的hashcode存在哪，如果重写equals不重写hashcode会怎么样？

反射的作用与实现原理；
Java中的回调机制；
模板方法模式；
开闭原则说一下；
发布/订阅使用场景；
KMP算法（一种改进的字符串匹配算法）；
JMM里边的原子性、可见性、有序性是如何体现出来的，JMM中内存屏障是什么意思，

二、多线程
AtomicInteger底层实现原理；
典型回答
AtomicInteger是对int类型的一个封装，提供原子性的访问和更新操作，其原子性操作的实现是基于CAS（compare-and-swap）技术。
所谓CAS，表现为一组指令，当利用CAS执行试图进行一些更新操作时。会首先比较当前数值，如果数值未变，代表没有其它线程进行并发修改，则成功更新。如果数值改变，则可能出现不同的选择，要么进行重试，要么就返回是否成功。也就是所谓的“乐观锁”。
从AtomicInteger的内部属性可以看出，它依赖于Unsafe提供的一些底层能力，进行底层操作；以volatile的value字段，记录数值，以保证可见性。
private static final jdk.internal.misc.Unsafe U = jdk.internal.misc.Unsafe.getUnsafe();
private static final long VALUE = U.objectFieldOffset(AtomicInteger.class, "value");
private volatile int value;
具体的原子操作细节，可以参考任意一个原子更新方法，比如下面的getAndIncrement。Unsafe会利用value字段的内存地址偏移，直接完成操作。

public final int getAndIncrement() {
  return U.getAndAddInt(this, VALUE, 1);
}
因为getAndIncrement需要返回数值，所以需要添加失败重试逻辑。

public final int getAndAddInt(Object o, long offset, int delta) {
  int v;
  do {
    v = getIntVolatile(o, offset);
  } while (!weakCompareAndSetInt(o, offset, v, v + delta));
  return v;
}
而类似compareAndSet这种返回boolean类型的函数，因为其返回值表现的就是是否成功与否，所以不需要重试。

public final boolean compareAndSet(int expectedValue, int newValue)
CAS是Java并发中所谓lock-free机制的基础。

知识扩展

接下来我们通过一个例子看看如何利用Java标准库使用CAS。设想这样一个场景：在数据库产品中，为保证索引的一致性，一个常见的选择是，保证只有一个线程能够排他性地修改一个索引分区。如何在数据库抽象层面实现呢？

可以考虑为索引分区对象添加一个逻辑上的锁。例如，以当前独占的线程ID作为锁的数值，然后通过原子操作设置lock数值，来实现加锁和释放锁，伪代码如下：

public class AtomicBTreePartition {
  private volatile long lock;
  public void acquireLock() {}
  public void releaseLock() {}
}
那么在Java代码中，我们怎么实现锁操作呢？Unsafe似乎不是个好的选择，它设计之初仅仅打算提供给Java标准库自己使用，它的名字也暗示我们最好不要用它。目前Java提供了两种公共API，可以实现这种CAS操作。比如使用java.util.concurrent.atomic.AtomicLongFieldUpdater，它是基于反射机制创建，我们需要保证类型和字段名正确。

private static final AtomicLongFieldUpdater<AtomicBTreePartition>
  LOCK_FIELD_UPDATER = AtomicLongFieldUpdater.newUpdater(
    AtomicBTreePartition.class,
    "lock");
 
private void acquireLock() {
  long t = Thread.currentThread().getId();
  while (!LOCK_FIELD_UPDATER.compareAndSet(this, 0L, t)) {
    // 等待一会儿，数据库操作可能比较慢。
    ...
  }
}
java.util.current.atomic包提供了最常用的原子性数据类型，甚至是引用、数组等相关原子类型和更新操作工具，是很多线程安全程序的首选。

如果是Java 9以后，我们完全可以采用另外一种方式实现，也就是Variable Handler API，提供了各种粒度的原子或有序性的操作。将前面的代码修改如下：

private static final VarHandle HANDLER = MethodHandles
  .lookup()
  .findStaticVarHandle(AtomicBTreePartition.class, "lock");
 
private void acquireLock() {
  long t = Thread.currentThread().getId();
  while (!HANDLE.compareAndSet(this, 0L, t)) {
    // 等待一会儿，数据库操作可能比较慢。
    ...
  }
}
过程非常直观，首先，获取相应的变量句柄，然后直接调用其提供的CAS方法。

一般来说，我们进行的类似CAS操作，可以并且推荐使用Variable Handler API去实现，其提供了精细粒度的公共底层API。这里强调“公共”，是因为其API不会内部API（例如Unsafe）那样，发生不可预测的修改。

CAS也并不是没有副作用。试想，其常用的失败重试机制，隐含着一个假设，即竞争情况是短暂的。大多数应用场景中，确实大部分重试只会发生一次就获得了成功。但是总是有意外情况，所以在有需要的时候，还是要考虑限制自旋的次数，以免过度消耗CPU。

另外一个就是著名的ABA问题，这是通常只在lock-free算法下暴露的问题。前面说过CAS是在更新时比较前值，如果对方只是恰好相同，例如期间发生了A->B->A的更新，仅仅判断数值是A，可能导致不合理的修改操作。针对这种情况，Java提供了AtomicStampedReference工具类。通过为引用建立类似版本号（stamp）的方式，来保证CAS的正确性。

前面介绍了CAS的场景与实现，幸运的是，大多数情况下，Java开发者并不需要直接利用CAS代码去实现线程安全容器等，更多是通过并发包等间接享受到lock-free机制在扩展性上的好处。

synchronized与ReentraLock哪个是公平锁；
CAS机制会出现什么问题；
用过并发包下边的哪些类；
一个线程连着调用start两次会出现什么情况？
wait方法能不能被重写，wait能不能被中断；
线程池的实现？四种线程池？重要参数及原理？任务拒接策略有哪几种？
线程状态以及API怎么操作会发生这种转换；
常用的避免死锁方法；
什么是线程？
线程和进程有什么区别？
如何在Java中实现线程？
Java 关键字volatile 与 synchronized 作用与区别？
有哪些不同的线程生命周期？
你对线程优先级的理解是什么？
什么是死锁(Deadlock)？如何分析和避免死锁？
什么是线程安全？Vector是一个线程安全类吗？ 
Java中如何停止一个线程？
什么是ThreadLocal?
Sleep()、suspend()和wait()之间有什么区别？
什么是线程死锁，什么是活锁？
什么是Java Timer类？如何创建一个有特定时间间隔的任务？
Java中的同步集合与并发集合有什么区别？
同步方法和同步块，哪个是更好的选择？
什么是线程池？ 为什么要使用它？
Java中invokeAndWait 和 invokeLater有什么区别？
多线程中的忙循环是什么?

三、JVM
Minor GC与Full GC分别在什么时候发生？什么时候触发Full GC;
GC收集器有哪些？CMS收集器与G1收集器的特点。
Java在什么时候会出现内存泄漏；
Java中的大对象如何进行存储；
rt.jar被什么类加载器加载，什么时间加载；
自己写的类被什么加载，什么时间加载；
自己写的两个不同的类是被同一个类加载器加载的吗？为什么？
为什么新生代内存需要有两个Survivor区？
几种常用的内存调试工具：jmap、jstack、jconsole；
类加载的五个过程：加载、验证、准备、解析、初始化；
G1停顿吗，CMS回收步骤，CMS为什么会停顿，停顿时间；
栈主要存的数据是什么，堆呢？
堆分为哪几块，比如说新生代老生代，那么新生代又分为什么？
软引用和弱引用的使用场景（软引用可以实现缓存，弱引用可以用来在回调函数中防止内存泄露）；

四、数据库
数据库索引，什么是全文索引，全文索引中的倒排索引是什么原理；
数据库最佳左前缀原则是什么？
数据库的三大范式；
悲观锁和乐观锁的原理和应用场景；
左连接、右连接、内连接、外连接、交叉连接、笛卡儿积等；
一般情况下数据库宕机了如何进行恢复（什么是Write Ahead Log机制，什么是Double Write机制，什么是Check Point）；
什么是redo日志、什么是undo日志；
数据库中的隔离性是怎样实现的；原子性、一致性、持久性又是如何实现的；
什么是组合索引，组合索引什么时候会失效；
关系型数据库和非关系型数据库区别；
数据库死锁如何解决；
MySQL并发情况下怎么解决（通过事务、隔离级别、锁）；
MySQL中的MVCC机制是什么意思，根据具体场景，MVCC是否有问题；
MySQL数据库的隔离级别，以及如何解决幻读；
JDBC访问数据库的基本步骤是什么？
说说preparedStatement和Statement的区别
说说事务的概念，在JDBC编程中处理事务的步骤。
数据库连接池的原理。为什么要使用连接池。
JDBC的脏读是什么？哪种数据库隔离级别能防止脏读？
什么是幻读，哪种隔离级别可以防止幻读？
JDBC的DriverManager是用来做什么的？
execute，executeQuery，executeUpdate的区别是什么？
SQL查询出来的结果分页展示一般怎么做？
JDBC的ResultSet是什么？

五、缓存服务器
Redis中zSet跳跃表问题；
Redis的set的应用场合？
Redis高级特性了解吗？
Redis的pipeline有什么用处？
Redis集群宕机如何处理，怎么样进行数据的迁移；
Redis的集群方案；
Redis原子操作怎么用比较好；
Redis过期策略是怎么实现的呢？

六、框架相关（SSM,SSH)
Spring中@Autowired和@Resource注解的区别？
Spring声明一个 bean 如何对其进行个性化定制；
MyBatis有什么优势；
MyBatis如何做事务管理；
谈谈你对Struts的理解。
谈谈你对Hibernate的理解。
谈谈你对Spring的理解。
谈谈Struts的优缺点
iBatis与Hibernate有什么不同?
在hibernate进行多表查询每个表中各取几个字段，也就是说查询出来的结果集没有一个实体类与之对应如何解决？
JDO是什么?
Hibernate的一对多和多对一双向关联的区别？？
Hibernate是如何延迟加载?
使用Spring框架的好处是什么？
ApplicationContext通常的实现是什么?
什么是Spring的依赖注入？有哪些方法进行依赖注入
什么是Spring beans?
解释Spring支持的几种bean的作用域。
解释Spring框架中bean的生命周期。
在 Spring中如何注入一个java集合？
解释不同方式的自动装配 。
Spring框架的事务管理有哪些优点？
什么是基于Java的Spring注解配置? 给一些注解的例子？
什么是ORM？
Hibernate中SessionFactory是线程安全的吗？Session是线程安全的吗（两个线程能够共享同一个Session吗）？
Session的save()、update()、merge()、lock()、saveOrUpdate()和persist()方法分别是做什么的？有什么区别？
阐述Session加载实体对象的过程。
MyBatis中使用#和$书写占位符有什么区别？
解释一下MyBatis中命名空间（namespace）的作用。
MyBatis中的动态SQL是什么意思？
JDBC编程有哪些不足之处，MyBatis是如何解决这些问题的？
MyBatis与Hibernate有哪些不同？
简单的说一下MyBatis的一级缓存和二级缓存？
说一说Servlet的生命周期?
Servlet API中forward()与redirect()的区别？
request.getAttribute()和 request.getParameter()有何区别?
jsp静态包含和动态包含的区别
MVC的各个部分都有那些技术来实现?如何实现?
jsp有哪些内置对象?作用分别是什么?
Http中，get和post方法的区别
什么是cookie？Session和cookie有什么区别？
jsp和servlet的区别、共同点、各自应用的范围？
tomcat容器是如何创建servlet类实例？用到了什么原理？

七、操作系统
Linux静态链接和动态链接；
什么是IO多路复用模型（select、poll、epoll）；
Linux中的grep管道用处？Linux的常用命令？
操作系统中虚拟地址、逻辑地址、线性地址、物理地址的概念及区别；
内存的页面置换算法；
内存的页面置换算法；
进程调度算法，操作系统是如何调度进程的；
父子进程、孤儿进程、僵死进程等概念；
fork进程时的操作；
kill用法，某个进程杀不掉的原因（僵死进程；进入内核态，忽略kill信号）；
系统管理命令（如查看内存使用、网络情况）；
find命令、awk使用；
Linux下排查某个死循环的线程；

八、网络相关
数据链路层是做什么的?
数据链路层的流量控制？
网络模型的分层、IP和Mac地址在那个层、TCP和HTTP分别在那个层；
TCP滑动窗口；
TCP为什么可靠；
TCP的同传，拆包与组装包是什么意思；
Https和Http有什么区别；
Http 为什么是无状态的；
TCP三次握手，为什么不是三次，为什么不是四次；
TCP的拥塞控制、流量控制详细说明？
Http1.0和Http2.0的区别；
两个不同ip地址的计算机之间如何通信；
地址解析协议ARP；
OSI七层模型分别对应着五层模型的哪一部分；
TCP三次握手数据丢失了怎么办？那如果后面又找到了呢？

九、分布式相关
消息队列使用的场景介绍和作用（应用耦合、异步消息、流量削锋等）；
如何解决消息队列丢失消息和重复消费问题；
Kafka使用过吗，什么是幂等性？怎么保证一致性，持久化怎么做，分区partition的理解，LEO是什么意思，如何保证多个partition之间数据一致性的（ISR机制），为什么Kafka可以这么快（基于磁盘的顺序读写）；
异步队列怎么实现；
你项目的并发是多少？怎么解决高并发问题？单机情况下Tomcat的并发大概是多少，MySQL的并发大致是多少？
什么是C10K问题；
高并发情况下怎么办；
分布式理论，什么是CAP理论，什么是Base理论，什么是Paxos理论；
CAP是一致性(Consistency)，可用性(Availability)，分区容错性(Partition tolerance)的缩写。在分布式系统设计中，任何一个系统都不可能同时满足这三个特性，只能满足其中2个特性。既然是分布式系统，所以分区容错性是必须要有的，所以在实际设计过程中，更多的是在一致性和可用性之间寻找平衡。
CAP三个特性的具体含义如下：
2.1. 一致性：这里说的一致性是指强一致性。分布式系统中，数据都是以多副本的形式存储在不同的节点中，要任何时刻数据副本之间的状态保持一致性。即一个进程对一个节点中的数据进行了更新后，同时其他节点中的数据副本实时也进行了更新。另一个进程读取其他节点的数据是最新版本。
2.2. 可用性：是指在任何时刻系统提供的服务都是可用的。用户或者客户端发送一个请求，服务总是在规定的时间内返回请求结果。在规定的时间内，没有返回请求结果，则认为服务不可用。
2.3. 分区容错性：系统在任何时刻发生网络故障，系统宕机，整个系统仍然能够对外提供可用性服务和一致性服务。这也说明了，在分布式系统设计过程中，更多的是考虑可用性和一致性平衡。
BASE理论
BASE理论是ebay架构师提出的。主要是指基本可用(Basically Available),软状态(soft state)和最终一致性(Eventually consistent)三个词的缩写。核心思想是即使无法做到强一致性，但是每个应用都可以根据自身业务特点，采用合适的方式达到最终一致性。
Base理论是依据大型的分布式系统架构设计设计经验得来，是一致性和可用性平衡结果。同时，也可以反映出在分布式系统设计中，主要考虑到PA两个特性，最终实现数据一致性。
BASE理论三个要素：
3.1. 基本可用：基本可用是指允许发生不可预知的故障，但是服务对外照常可用。只是损失部分可用性。
3.2. 弱状态 ：数据副本在整个服务运行期间，允许在某一个时刻(比如，数据复制)允许存在中间状态，但是最终数据的状态保持一致。在处于弱状态时，整个服务可用。
3.3 最终一致性：在经过一定的时间后，所有数据副本的状态都已经保持一致。一致性的本质就是要保证数据最终一致性。
小结
Base/CAP理论，在分布式系统建设中提供了一个大的原则和方向。具体的设计过程中，还需要根据不同的系统的业务特点，灵活去设计，考虑的侧重点不同。


分布式协议的选举算法；
说一下你对微服务的理解，与SOA的区别；
Dubbo的基本原理，RPC，支持哪些通信方式，服务的调用过程；
Dubbo如果有一个服务挂掉了怎么办；
分布式事务，操作两个表不在一个库，如何保证一致性。
分布式系统中，每台机器如何产生一个唯一的随机值；
系统的量级、pv、uv等；
什么是Hash一致性算法？分布式缓存的一致性，服务器如何扩容（哈希环）；
正向代理、反向代理；
什么是客户端负载均衡策略、什么是服务器端负载均衡策略；
如何优化Tomcat，常见的优化方式有哪些；
Nginx的Master和Worker，Nginx是如何处理请求的；

十、系统设计相关
如何防止表单重复提交（Token令牌环等方式）；
有一个url白名单，需要使用正则表达式进行过滤，但是url量级很大，大概亿级，那么如何优化正则表达式？如何优化亿级的url匹配呢？
常见的Nginx负载均衡策略；已有两台Nginx服务器了，倘若这时候再增加一台服务器，采用什么负载均衡算法比较好？
扫描二维码登录的过程解析；
如何设计一个生成唯一UUID的算法？
实现一个负载均衡的算法，服务器资源分配为70%、20%、10%；
有三个线程T1 T2 T3，如何保证他们按顺序执行；
三个线程循环输出ABCABCABC....

十一、安全相关
什么是XSS攻击，XSS攻击的一般表现形式有哪些？如何防止XSS攻击；
