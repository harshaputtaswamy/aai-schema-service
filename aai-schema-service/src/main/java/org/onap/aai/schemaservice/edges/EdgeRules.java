/**
 * ============LICENSE_START=======================================================
 * org.onap.aai
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.aai.schemaservice.edges;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class EdgeRules {

    @SerializedName("rules")
    private List<EdgeRule> rules;

    public EdgeRules(List<EdgeRule> rules) {
        this.rules = rules;
    }

    public List<EdgeRule> getRules() {
        return rules;
    }

    public void setRules(List<EdgeRule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EdgeRules rules1 = (EdgeRules) o;

        return Objects.equals(rules, rules1.rules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rules);
    }

    @Override
    public String toString() {
        return "EdgeRules{" + "rules=" + rules + '}';
    }

}
