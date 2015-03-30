/*
 *    Copyright 2013-2014 Parisoft Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.parisoft.resty.response;

import org.apache.http.HttpResponse;

import com.github.parisoft.resty.entity.EntityReader;
import com.github.parisoft.resty.response.http.HttpResponseExtension;

/**
 * Interface that wraps the {@link HttpResponse} and contains useful methods do deal with a HTTP response entity.
 *
 * @author Andre Paris
 *
 */
public interface Response extends HttpResponse, HttpResponseExtension, EntityReader {

}
