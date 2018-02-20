/*
 * Copyright 2018 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.bazel.ruleskotlin.integrationtests.jvm

import io.bazel.ruleskotlin.integrationtests.lib.AssertionTestCase
import org.junit.Test


class JvmBasicIntegrationTests : AssertionTestCase("tests/integrationtests/jvm/basic") {
    @Test
    fun testResourceMerging() {
        jarTestCase("test_embed_resources.jar", description = "The rules should support including resource directories") {
            assertContainsEntries(
                    "testresources/AClass.class",
                    "testresources/BClass.class",
                    "tests/integrationtests/jvm/basic/testresources/resources/one/alsoAFile.txt",
                    "tests/integrationtests/jvm/basic/testresources/resources/one/two/aFile.txt"
            )
        }
        jarTestCase("test_merge_resourcesjar.jar", description = "the rules should support merging jars") {
            assertContainsEntries("testresources/AClass.class", "testresources/BClass.class", "pkg/file.txt")
        }
        jarTestCase("test_embed_resources_strip_prefix.jar", description = "the rules should support the resource_strip_prefix attribute") {
            assertContainsEntries("testresources/AClass.class", "testresources/BClass.class", "one/two/aFile.txt", "one/alsoAFile.txt")
        }
        jarTestCase("conventional_strip_resources.jar", description = "the rules should support conventional prefix stripping") {
            assertContainsEntries("main.txt", "test.txt")
        }
    }

    @Test
    fun testPropogateDeps() {
        assertExecutableRunfileSucceeds(
                "propagation_rt_via_export_consumer",
                description = "Runtime deps should be inherited transitively from `exported` deps"
        )
        assertExecutableRunfileSucceeds(
                "propagation_rt_via_runtime_deps_consumer",
                description = "Runtime deps should be inherited transitively from `runtime_deps`"
        )
    }

    @Test
    fun testModuleNaming() {
        jarTestCase("test_module_name_bin.jar", description = "A binary rule should support default module naming") {
            assertContainsEntries("META-INF/tests_integrationtests_jvm_basic-test_module_name_bin.kotlin_module")
        }
        jarTestCase("test_module_name_lib.jar", description = "A library rule should support default module naming") {
            assertContainsEntries("META-INF/tests_integrationtests_jvm_basic-test_module_name_lib.kotlin_module")
        }
        jarTestCase("test_module_name_attr_lib.jar", description = "The kotlin rules should support the module_name attribute") {
            assertContainsEntries("META-INF/hello-module.kotlin_module")
        }
    }
}
