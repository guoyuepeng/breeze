// THIS IS AN AUTO-GENERATED FILE. DO NOT MODIFY.    

/*
 Copyright 2009 David Hall, Daniel Ramage
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at 
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License. 
*/

// generated by GenCounter on Sat Jan 17 14:49:38 PST 2009
package scalanlp.counters.doubles;

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

/**
 * Count objects of type Double with type Short.
 * This trait is a wrapper around Scala's Map trait
 * and can work with any scala Map. 
 *
 * @author dlwh
 */
@serializable 
trait Double2ShortCounter extends ShortCounter[Double] {


  abstract override def update(k : Double, v : Short) = {

    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : Double, v : Short) :Option[Short] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : Double) = {

    super.-=(key);
  }

  /**
   * Increments the count by the given parameter.
   */
   override  def incrementCount(t : Double, v : Short) = {
     update(t,(this(t) + v).asInstanceOf[Short]);
   }


  override def ++=(kv: Iterable[(Double,Short)]) = kv.foreach(+=);

  /**
   * Increments the count associated with Double by Short.
   * Note that this is different from the default Map behavior.
  */
  override def +=(kv: (Double,Short)) = incrementCount(kv._1,kv._2);

  override def default(k : Double) : Short = 0;

  override def apply(k : Double) : Short = super.apply(k);

  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): Double2ShortCounter  = super.clone().asInstanceOf[Double2ShortCounter]

  /**
   * Return the Double with the largest count
   */
  override  def argmax() : Double = (elements reduceLeft ((p1:(Double,Short),p2:(Double,Short)) => if (p1._2 > p2._2) p1 else p2))._1

  /**
   * Return the Double with the smallest count
   */
  override  def argmin() : Double = (elements reduceLeft ((p1:(Double,Short),p2:(Double,Short)) => if (p1._2 < p2._2) p1 else p2))._1

  /**
   * Return the largest count
   */
  override  def max : Short = values reduceLeft ((p1:Short,p2:Short) => if (p1 > p2) p1 else p2)
  /**
   * Return the smallest count
   */
  override  def min : Short = values reduceLeft ((p1:Short,p2:Short) => if (p1 < p2) p1 else p2)

  // TODO: decide is this is the interface we want?
  /**
   * compares two objects by their counts
   */ 
  override  def comparator(a : Double, b :Double) = apply(a) compare apply(b);

  /**
   * Return a new Double2DoubleCounter with each Short divided by the total;
   */
  override  def normalized() : Double2DoubleCounter = {
    val normalized = Double2DoubleCounter();
    val total : Double = this.total
    if(total != 0.0)
      for (pair <- elements) {
        normalized(pair._1) = pair._2 / total;
      }
    normalized
  }

  /**
   * Return the sum of the squares of the values
   */
  override  def l2norm() : Double = {
    var norm = 0.0
    for (val v <- values) {
      norm += (v * v)
    }
    return Math.sqrt(norm)
  }

  /**
   * Return a List the top k elements, along with their counts
   */
  override  def topK(k : Int) = Counters.topK[(Double,Short)](k,(x,y) => if(x._2 < y._2) -1 else if (x._2 == y._2) 0 else 1)(this);

  /**
   * Return \sum_(t) C1(t) * C2(t). 
   */
  def dot(that : Double2ShortCounter) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += get(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : Double2ShortCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Short]);
    }
  }

  def -=(that : Double2ShortCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) - v).asInstanceOf[Short]);
    }
  }

  override  def *=(scale : Short) {
    transform { (k,v) => (v * scale).asInstanceOf[Short]}
  }

  override  def /=(scale : Short) {
    transform { (k,v) => (v / scale).asInstanceOf[Short]}
  }
}


object Double2ShortCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._


  import scala.collection.jcl.MapWrapper;
  @serializable
  @SerialVersionUID(1L)
  class FastMapCounter extends MapWrapper[Double,Short] with Double2ShortCounter {
    private val under = new Double2ShortOpenHashMap;
    def underlying() = under.asInstanceOf[java.util.Map[Double,Short]];
    override def apply(x : Double) = under.get(x);
    override def update(x : Double, v : Short) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  def apply() = new FastMapCounter();

  
}

