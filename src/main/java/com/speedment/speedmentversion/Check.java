/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.speedmentversion;

import static com.speedment.speedmentversion.util.VersionUtil.compareVersions;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * The main generator
 *
 * @author Per Minborg
 * @author Emil Forslund
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.COMPILE)
public class Check extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;
    
    @Parameter(defaultValue = "0")
    private String minVersion;

    @Parameter(defaultValue = "false")
    private boolean debug;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (debug) {
            getLog().info("Running in debug mode.");
        }
        
        getLog().info(
            "Analyzing " + mavenProject.getName() + 
            " (" + mavenProject.getGroupId() + 
            ":" + mavenProject.getArtifactId() + ")"
        );
        
        if (!printVersionTree()) {
            throw new MojoFailureException(
                "Make sure every -SNAPSHOT dependency has been replaced with " + 
                "a stable version number."
            );
        }
    }
    
    private boolean printVersionTree() {
        return printVersionTree(mavenProject, 0);
    }
    
    private boolean printVersionTree(MavenProject project, int level) {
        if (project.getVersion() == null) {
            getLog().error("Could not find version of ancestor '" + project.getArtifactId() + "'.");
            return false;
        }
        
        boolean success = true;
        final String branch = formatBranch(project.getArtifactId(), project.getVersion(), level);
        if (!validateVersion(project.getVersion())) {
            getLog().error(branch);
            success = false;
        } else if (project.getVersion().endsWith("-SNAPSHOT")) {
            getLog().warn(branch);
            success = false;
        } else {
            getLog().info(branch);
        }
        
        final MavenProject parent = project.getParent();
        if (parent != null) {
            if (!printVersionTree(parent, level + 1)) {
                success = false;
            }
        }
        
        return success;
    }
    
    private boolean validateVersion(String version) {
        return compareVersions(version, minVersion) >= 0;
    }
    
    private final static int MAX_WIDTH = 80;
    private final static int TAB_SIZE = 4;
    private static String formatBranch(String artifactId, String version, int level) {
        final StringBuilder str = new StringBuilder();
        
        final int tabAmount = level * TAB_SIZE;
        for (int i = 0; i < tabAmount; i++) {
            str.append(' ');
        }
        
        str.append(artifactId);
        final int dots = MAX_WIDTH - tabAmount - artifactId.length() - version.length();
        for (int i = 0; i < dots; i++) {
            str.append('.');
        }
        
        str.append(version);
        return str.toString();
    }
}