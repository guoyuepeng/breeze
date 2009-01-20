package scalanlp.stats.sampling;

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

import math.Numerics._;
import Math._;

class Poisson(val mean: Double) extends DiscreteDistr[Int] {
  private val ell = Math.exp(-mean);
  //  TODO: this is from Knuth, but it's linear in mean.
  def draw() = {
    var k = 0;
    var p = 1.;
    do { 
      k += 1;
      p *= Rand.uniform.get();
    } while(p >= ell);
    k - 1;
  }

  def probabilityOf(k:Int) = Math.exp(logProbabilityOf(k));
  override def logProbabilityOf(k:Int) = {
    -mean + k * log(mean) - lgamma(k);
  }
}
