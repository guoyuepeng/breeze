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
package scalanlp.counters.longs;

import scala.collection.mutable.Map;
import scala.collection.mutable.HashMap;

/**
 * Count objects of type Long with type Float.
 * This trait is a wrapper around Scala's Map trait
 * and can work with any scala Map. 
 *
 * @author dlwh
 */
@serializable 
trait Long2FloatCounter extends FloatCounter[Long] {


  abstract override def update(k : Long, v : Float) = {

    super.update(k,v);
  }

  // this isn't necessary, except that the jcl MapWrapper overrides put to call Java's put directly.
  override def put(k : Long, v : Float) :Option[Float] = { val old = get(k); update(k,v); old}

  abstract override def -=(key : Long) = {

    super.-=(key);
  }

  /**
   * Increments the count by the given parameter.
   */
   override  def incrementCount(t : Long, v : Float) = {
     update(t,(this(t) + v).asInstanceOf[Float]);
   }


  override def ++=(kv: Iterable[(Long,Float)]) = kv.foreach(+=);

  /**
   * Increments the count associated with Long by Float.
   * Note that this is different from the default Map behavior.
  */
  override def +=(kv: (Long,Float)) = incrementCount(kv._1,kv._2);

  override def default(k : Long) : Float = 0;

  override def apply(k : Long) : Float = super.apply(k);

  // TODO: clone doesn't seem to work. I think this is a JCL bug.
  override def clone(): Long2FloatCounter  = super.clone().asInstanceOf[Long2FloatCounter]

  /**
   * Return the Long with the largest count
   */
  override  def argmax() : Long = (elements reduceLeft ((p1:(Long,Float),p2:(Long,Float)) => if (p1._2 > p2._2) p1 else p2))._1

  /**
   * Return the Long with the smallest count
   */
  override  def argmin() : Long = (elements reduceLeft ((p1:(Long,Float),p2:(Long,Float)) => if (p1._2 < p2._2) p1 else p2))._1

  /**
   * Return the largest count
   */
  override  def max : Float = values reduceLeft ((p1:Float,p2:Float) => if (p1 > p2) p1 else p2)
  /**
   * Return the smallest count
   */
  override  def min : Float = values reduceLeft ((p1:Float,p2:Float) => if (p1 < p2) p1 else p2)

  // TODO: decide is this is the interface we want?
  /**
   * compares two objects by their counts
   */ 
  override  def comparator(a : Long, b :Long) = apply(a) compare apply(b);

  /**
   * Return a new Long2DoubleCounter with each Float divided by the total;
   */
  override  def normalized() : Long2DoubleCounter = {
    val normalized = Long2DoubleCounter();
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
  override  def topK(k : Int) = Counters.topK[(Long,Float)](k,(x,y) => if(x._2 < y._2) -1 else if (x._2 == y._2) 0 else 1)(this);

  /**
   * Return \sum_(t) C1(t) * C2(t). 
   */
  def dot(that : Long2FloatCounter) : Double = {
    var total = 0.0
    for (val (k,v) <- that.elements) {
      total += get(k).asInstanceOf[Double] * v
    }
    return total
  }

  def +=(that : Long2FloatCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) + v).asInstanceOf[Float]);
    }
  }

  def -=(that : Long2FloatCounter) {
    for(val (k,v) <- that.elements) {
      update(k,(this(k) - v).asInstanceOf[Float]);
    }
  }

  override  def *=(scale : Float) {
    transform { (k,v) => (v * scale).asInstanceOf[Float]}
  }

  override  def /=(scale : Float) {
    transform { (k,v) => (v / scale).asInstanceOf[Float]}
  }
}


object Long2FloatCounter {
  import it.unimi.dsi.fastutil.objects._
  import it.unimi.dsi.fastutil.ints._
  import it.unimi.dsi.fastutil.shorts._
  import it.unimi.dsi.fastutil.longs._
  import it.unimi.dsi.fastutil.floats._
  import it.unimi.dsi.fastutil.doubles._


  import scala.collection.jcl.MapWrapper;
  @serializable
  @SerialVersionUID(1L)
  class FastMapCounter extends MapWrapper[Long,Float] with Long2FloatCounter {
    private val under = new Long2FloatOpenHashMap;
    def underlying() = under.asInstanceOf[java.util.Map[Long,Float]];
    override def apply(x : Long) = under.get(x);
    override def update(x : Long, v : Float) {
      val oldV = this(x);
      updateTotal(v-oldV);
      under.put(x,v);
    }
  }

  def apply() = new FastMapCounter();

  
}

