import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf

/**
 *
 * Copyright Careem, an Uber Technologies Inc. company
 *
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
 */

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../versions.toml"))
        }
    }
}


gradle.rootProject {
    val accessors = files(serviceOf<DependenciesAccessors>().classes.asFiles)

    // To silent the IDE missing import
    buildscript { dependencies.classpath(accessors) }

    // To add version catalog as plugin dependency
    configurations
        .matching { it.name == "implementation" }
        .configureEach {
            val implementation = this
            dependencies { implementation(accessors) }
        }
}