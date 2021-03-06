/*
 * SCALA LICENSE
 *
 * Copyright (C) 2011-2012 Artima, Inc. All rights reserved.
 *
 * This software was developed by Artima, Inc.
 *
 * Permission to use, copy, modify, and distribute this software in source
 * or binary form for any purpose with or without fee is hereby granted,
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the EPFL nor the names of its contributors
 *    may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package scala.tools.eclipse.scalatest.launching

import scala.io.Source
import java.net.{URL, URLClassLoader}
import java.io.File

object ScalaTestLauncher {

  def main(args: Array[String]) {
    try {
      val cpFilePath = args(0)
      val classpath = Source.fromFile(args(0)).getLines()
    
      val loader = ClassLoader.getSystemClassLoader
      val method= classOf[URLClassLoader].getDeclaredMethod("addURL", classOf[URL]); //$NON-NLS-1$
      method.setAccessible(true);
      classpath.foreach(cp => method.invoke(loader, new File(cp.toString).toURI.toURL))
    
      val runnerClass = Class.forName("org.scalatest.tools.Runner")
      val mainMethod = runnerClass.getMethod("main", args.getClass()) //$NON-NLS-1$
      mainMethod.setAccessible(true)
      mainMethod.invoke(null, Source.fromFile(args(1)).getLines().toArray)
    }
    catch {
      case e: Throwable => e.printStackTrace()
    }
    finally {
      val cpFile = new File(args(0))
      cpFile.delete()
      val argsFile = new File(args(1))
      argsFile.delete()
    }
  }
  
}