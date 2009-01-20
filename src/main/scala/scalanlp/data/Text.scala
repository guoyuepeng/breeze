package scalanlp.data;

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


import scalax.io.Implicits._;
import java.io.File;
import counters.Counters._;


/**
* Represents a sequence of text. This is just a string with an id, a nice map method.
*
* @author dlwh
*/
case class Text(val id:String, val contents: String) extends Observation[String] {
  def features = contents;
   
  def withLabel[L](l:L) = new LabeledText[L](id,l,contents);
}

object Text {
 def fromFile(f :File) = {
   new Text(f.getName,f.slurp);
 }
}

class LabeledText[L](id:String,val label:L, contents: String) extends Text(id,contents) with Example[L,String];

object LabeledText {
  def fromFile(f:File) = {
    new Text(f.getName,f.slurp).withLabel(f.getParentFile.getName);
  }
}
