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
package com.github.parisoft.resty.exception;

import java.io.IOException;

/**
 * Thrown when got an unexpected HTTP response status code.
 *
 * @author Andre Paris
 *
 */
public class IllegalHttpStatusCode extends IOException {

    private static final long serialVersionUID = 137106421226523687L;

    private final int expectedStatusCode;
    private final int actualStatusCode;

    public IllegalHttpStatusCode(int expectedStatus, int actualStatus) {
        super(String.format("Cannot assert response status code: expected %s, got %s", expectedStatus, actualStatus));
        this.expectedStatusCode = expectedStatus;
        this.actualStatusCode = actualStatus;
    }

    public int getActualStatusCode() {
        return actualStatusCode;
    }

    public int getExpectedStatusCode() {
        return expectedStatusCode;
    }
}
