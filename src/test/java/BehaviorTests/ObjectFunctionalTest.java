/**
 * The MIT License
 *
 * Copyright for portions of OpenUnirest/uniresr-java are held by Mashape (c) 2013 as part of Kong/unirest-java.
 * All other copyright for OpenUnirest/unirest-java are held by OpenUnirest (c) 2018.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package BehaviorTests;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.junit.Test;
import unirest.Unirest;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;

public class ObjectFunctionalTest extends BddTest {
    private Gson gson = new Gson();

    @Test
    public void canUseAFunctionToTransform() {
        MockServer.setJsonAsResponse(of("foo", "bar"));

        Map r = Unirest.get(MockServer.GET)
                .queryString("foo", "bar")
                .asObject(i -> gson.fromJson(i.getContentReader(), HashMap.class))
                .getBody();

        assertEquals("bar", r.get("foo"));
    }

    @Test
    public void canUseAFunctionToTransformAsync() throws Exception {
        MockServer.setJsonAsResponse(of("foo", "bar"));

        Map r = Unirest.get(MockServer.GET)
                .queryString("foo", "bar")
                .asObjectAsync(i -> gson.fromJson(i.getContentReader(), HashMap.class))
                .get()
                .getBody();

        assertEquals("bar", r.get("foo"));
    }

    @Test
    public void canUseAFunctionToTransformAsyncWithCallback() throws Exception {
        MockServer.setJsonAsResponse(of("foo", "bar"));

        Unirest.get(MockServer.GET)
                .queryString("foo", "bar")
                .asObjectAsync(i -> gson.fromJson(i.getContentReader(), HashMap.class),
                        r -> {
                            HashMap map = r.getBody();
                            assertEquals("bar", map.get("foo"));
                            asyncSuccess();
                        });

        assertAsync();
    }
}