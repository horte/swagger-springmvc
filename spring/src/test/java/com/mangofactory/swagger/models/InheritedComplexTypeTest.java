package com.mangofactory.swagger.models;

import com.fasterxml.classmate.TypeResolver;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.wordnik.swagger.model.Model;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.Maps.*;
import static com.mangofactory.swagger.models.ResolvedTypes.asResolvedType;
import static org.junit.Assert.*;

public class InheritedComplexTypeTest {
    private Map<String, Model> modelMap;

    class Named {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class Pet extends Named {
        int age;
        Category category;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    class Category {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Before
    public void setup() {
        modelMap = newHashMap();
        DocumentationSchemaProvider provider = new DocumentationSchemaProvider(new TypeResolver(),
                new SwaggerConfiguration("1.1", "/"));
        modelMap = provider.getModelMap(new Model("Pet", asResolvedType(Pet.class)));
    }

    @Test
    public void hasExpectedModels() {
        assertEquals(2, modelMap.size());
    }

    @Test
    public void hasAPetModel() {
        assertTrue(modelMap.containsKey("Pet"));
        Model pet = modelMap.get("Pet");
        assertNotNull(pet.getProperties());
        assertEquals(3, pet.getProperties().size());
    }

    @Test
    public void hasACategoryModel() {
        assertTrue(modelMap.containsKey("Category"));
        Model category = modelMap.get("Category");
        assertNotNull(category.getProperties());
        assertEquals(1, category.getProperties().size());
    }

    @Test
    public void schemaHasAStringProperty() {
        Model schema = modelMap.get("Pet");
        assertTrue(schema.getProperties().containsKey("name"));
        Model stringProperty = schema.getProperties().get("name");
        assertNotNull(stringProperty);
        assertEquals("string", stringProperty.getType());
    }

    @Test
    public void schemaHasAIntProperty() {
        Model schema = modelMap.get("Pet");
        assertTrue(schema.getProperties().containsKey("age"));
        Model stringProperty = schema.getProperties().get("age");
        assertNotNull(stringProperty);
        assertEquals("int", stringProperty.getType());
    }

    @Test
    public void schemaHasACategoryProperty() {
        Model schema = modelMap.get("Pet");
        assertTrue(schema.getProperties().containsKey("category"));
        Model stringProperty = schema.getProperties().get("category");
        assertNotNull(stringProperty);
        assertEquals("Category", stringProperty.getType());
    }

    @Test
    public void hasACategoryNameProperty() {
        Model category = modelMap.get("Category");
        assertTrue(category.getProperties().containsKey("name"));
    }

}