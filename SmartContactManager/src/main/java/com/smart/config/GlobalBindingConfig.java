package com.smart.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/*
*this advice run before every request
*@InitBinder = By default Spring automatically binds incoming request data (like form fields, query params, JSON, etc.) to Java objects binder.But Now this annotation allow you to handle Custom Binding 
* WebDataBinder  = the incoming data will store in this object. You can customizing how it handles certain types (here: String).
*registerCustomEditor() = this method specify that the incoming String data should be Trim whitespace from both side and convert empty string to null
* Without this:  Empty strings ("") are treated as valid values and store in DB, even if you use @NotNull on field, because not null only prevent null value should't be Store, but it allow empty string
* for prevention empty string we can use @NotBlank with @NotBlank but still if user add whitespace before and after the string, it will save as it as.
* so registerCustomEditor() method senitize the string (It do two things 1.Trims whiteSpace, 2.Convert empty String to null)
*example: Trims whitespace
                  " hello " → "hello"
        Converts empty strings to null
                  "" → null
                  " " → null (because trimmed)
*/

@ControllerAdvice
public class GlobalBindingConfig {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
