/*
Copyright 2011-2012 Opera Software ASA

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

package com.opera.core.systems;

import com.opera.core.systems.testing.Ignore;
import com.opera.core.systems.testing.OperaDriverTestCase;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import static com.opera.core.systems.OperaProduct.MOBILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.openqa.selenium.Platform.MAC;

public class JavascriptTest extends OperaDriverTestCase {

  @Before
  public void beforeEach() {
    driver.navigate().to(pages.javascript);
  }

  @Test
  public void testTyping() {
    String text = "Hello, world!";

    driver.executeScript("document.getElementById('one').focus()");
    new Actions(driver).sendKeys(text).perform();

    assertEquals(text, driver.findElement(By.id("one")).getAttribute("value"));
  }

  // Make sure that typing actually happens. When the focus switches half way
  // through typing we should continue typing on the other textbox.
  @Test
  @Ignore(products = MOBILE, value = "Needs investigation")
  public void testTypingKeyEvents() {
    driver.navigate().to(pages.keys);

    new Actions(driver).sendKeys("hi").perform();

    String log = driver.findElement(By.id("log")).getAttribute("value");
    assertTrue(log.contains("press, 104, h,"));
    assertTrue(log.contains("up, 73, I,"));
  }

  @Test
  @Ignore(platforms = MAC,
          value = "Needs investigation why JS doubleclick events are not triggered on Mac")
  public void testDoubleClick() {
    new Actions(driver).doubleClick(driver.findElement(By.id("one"))).perform();
    assertEquals(driver.findElement(By.id("two")).getAttribute("value"), "double");
  }

  @Test
  public void testWindowCount() throws Exception {
    int numWindows = driver.getWindowHandles().size();

    driver.findElement(By.id("open_window")).click();

    assertEquals(numWindows + 1, driver.getWindowHandles().size());
  }

  @Test
  @Ignore
  public void testWindowCount2() throws Exception {
    int numWindows = driver.getWindowHandles().size();

    driver.close();

    assertTrue(driver.getCurrentUrl().endsWith("javascript.html"));
    assertEquals(numWindows - 1, driver.getWindowHandles().size());
  }

}