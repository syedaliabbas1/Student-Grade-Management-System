package uk.ac.ucl.comp0010.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * Test class for Module entity.
 */
public class ModuleTest {

  @Test
  void testConstructorAndGetters() {
    // Arrange
    String code = "COMP0010";
    String name = "Software Engineering";
    Boolean mnc = true;

    // Act
    Module module = new Module(code, name, mnc);

    // Assert
    assertEquals(code, module.getCode(), "Code should be COMP0010");
    assertEquals(name, module.getName(), "Name should be Software Engineering");
    assertEquals(mnc, module.getMnc(), "MNC should be true");
  }

  @Test
  void testSetters() {
    // Arrange
    Module module = new Module("COMP0010", "Software Engineering", true);

    // Act
    module.setCode("COMP0020");
    module.setName("Computer Architecture");
    module.setMnc(false);

    // Assert
    assertEquals("COMP0020", module.getCode(), "Code should be COMP0020 after setting");
    assertEquals("Computer Architecture", module.getName(),
        "Name should be Computer Architecture after setting");
    assertFalse(module.getMnc(), "MNC should be false after setting");
  }

  @Test
  void testDefaultConstructor() {
    Module module = new Module();
    assertNull(module.getId());
    assertNull(module.getCode());
    assertNull(module.getName());
    assertFalse(module.getMnc()); // Default is false
  }


  @Test
  void testSetId() {
    // Arrange
    Module module = new Module("COMP0010", "Software Engineering", true);

    // Act
    module.setId(100L);

    // Assert
    assertEquals(100L, module.getId(), "ID should be 100 after setting");
  }

  @Test
  void testEdgeCaseForMandatoryField() {
    // Arrange
    Module module = new Module("COMP0030", "Advanced Software Engineering", false);

    // Act and Assert
    assertFalse(module.getMnc(), "MNC should be null if not set explicitly");
  }


  @Test
  void testParameterizedConstructor() {
    Module module = new Module("CS101", "Introduction to Programming", true);
    assertEquals("CS101", module.getCode());
    assertEquals("Introduction to Programming", module.getName());
    assertTrue(module.getMnc());
  }

  @Test
  void testSettersAndGetters() {
    Module module = new Module();
    module.setId(1L);
    module.setCode("CS102");
    module.setName("Data Structures");
    module.setMnc(false);

    assertEquals(1L, module.getId());
    assertEquals("CS102", module.getCode());
    assertEquals("Data Structures", module.getName());
    assertFalse(module.getMnc());
  }
}
