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
package com.speedment.speedmentversion.maven.plugin;

import com.speedment.enterprise.core.util.XMLUtil;
import static com.speedment.enterprise.core.util.XMLUtil.find;
import java.io.File;
import static java.lang.System.out;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The main generator
 *
 * @author Per Minborg
 */
@Mojo(name = "check", defaultPhase = LifecyclePhase.COMPILE)
public class Check extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    private @Parameter(defaultValue = "false")
    boolean debug;
//    private @Parameter(required = true)
//    String speedmentVersion;

    @Override
    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (debug) {
            getLog().info("debug on");
        }

        final Document rootPom = XMLUtil.loadDocumentFromFile(new File("pom.xml"));

        final Element settings = (Element) rootPom.getFirstChild();
        final Optional<Element> project = find(settings, "project");
        if (project.isPresent()) {
            final Optional<Element> version = find(project.get(), "version");
            System.out.println(version);
        }

        

//        Node project = null;
//        Node version = null;
//
//        final NodeList nl = rootPom.getChildNodes();
//
//        for (int i = 0; i < nl.getLength(); i++) {
//            final Node node = nl.item(i);
//            if ("project".equals(node.getNodeName())) {
//                project = node;
//                NodeList projectNodes = project.getChildNodes();
//                for (int j = 0; j < nl.getLength(); j++) {
//                    final Node projectNode = projectNodes.item(j);
////                    if ("version".equals(projectNode.getNodeName())) {
////                        version = projectNode;
////                    }
//                    NodeList projectSubNodes = projectNode.getChildNodes();
//                    for (int k = 0; k < projectSubNodes.getLength(); k++) {
//                        final Node subNode = projectSubNodes.item(k);
//                        if ("version".equals(subNode.getNodeName())) {
//                            version = projectNode;
//                        }
//                    }
//                }
//                // echo(node);
//            }
//        }
//        if (version == null) {
//            System.out.println("Version not found");
//        } else {
//            System.out.println("Version " + version.getNodeValue());
//        }
        //      String projectTag = project.getTagName();
//
        //      Node versionNode = project.getElementsByTagName("version").item(0);
//        
//        System.out.println(project);
//        System.out.println(rootName);
//
//        final NodeList nl = rootPom.getChildNodes();
//
//        Node project = null;
//        for (int i = 0; i < nl.getLength(); i++) {
//            final Node node = nl.item(i);
//            if ("project".equals(node.getNodeName())) {
//                project = node;
//            }
//            recurse(node);
//        }
//        
//        XMLUtil.find(parent, ROLE)
//        Optional<Element> project = XMLUtil.find(null, "project");
//        getLog().info("Project " + project);
//
//        Optional<Element> projectVersion = XMLUtil.find(project, "version");
//        getLog().info("ProjectVersion " + projectVersion);
        getLog().info("Checking speedment version ");

    }

    private int indent = 0;

    private void outputIndentation() {
        IntStream.range(0, indent).forEach(i -> System.out.print(" "));
    }

    private void printlnCommon(Node node) {
        out.println(node);
    }

    private void echo(Node n) {
        outputIndentation();
        int type = n.getNodeType();

        switch (type) {
            case Node.ATTRIBUTE_NODE:
                out.print("ATTR:");
                printlnCommon(n);
                break;

            case Node.CDATA_SECTION_NODE:
                out.print("CDATA:");
                printlnCommon(n);
                break;

            case Node.COMMENT_NODE:
                out.print("COMM:");
                printlnCommon(n);
                break;

            case Node.DOCUMENT_FRAGMENT_NODE:
                out.print("DOC_FRAG:");
                printlnCommon(n);
                break;

            case Node.DOCUMENT_NODE:
                out.print("DOC:");
                printlnCommon(n);
                break;

            case Node.DOCUMENT_TYPE_NODE:
                out.print("DOC_TYPE:");
                printlnCommon(n);
                NamedNodeMap nodeMap = ((DocumentType) n).getEntities();
                indent += 2;
                for (int i = 0; i < nodeMap.getLength(); i++) {
                    Entity entity = (Entity) nodeMap.item(i);
                    echo(entity);
                }
                indent -= 2;
                break;

            case Node.ELEMENT_NODE:
                out.print("ELEM:");
                printlnCommon(n);

                NamedNodeMap atts = n.getAttributes();
                indent += 2;
                for (int i = 0; i < atts.getLength(); i++) {
                    Node att = atts.item(i);
                    echo(att);
                }
                indent -= 2;
                break;

            case Node.ENTITY_NODE:
                out.print("ENT:");
                printlnCommon(n);
                break;

            case Node.ENTITY_REFERENCE_NODE:
                out.print("ENT_REF:");
                printlnCommon(n);
                break;

            case Node.NOTATION_NODE:
                out.print("NOTATION:");
                printlnCommon(n);
                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                out.print("PROC_INST:");
                printlnCommon(n);
                break;

            case Node.TEXT_NODE:
                out.print("TEXT:");
                printlnCommon(n);
                break;

            default:
                out.print("UNSUPPORTED NODE: " + type);
                printlnCommon(n);
                break;
        }

        indent++;
        for (Node child = n.getFirstChild(); child != null;
            child = child.getNextSibling()) {
            echo(child);
        }
        indent--;
    }

    private void recurse(Node parent) {
        final NodeList nl = parent.getChildNodes();
        System.out.println(parent);
        for (int i = 0; i < nl.getLength(); i++) {
            final Node child = nl.item(i);
            recurse(child);
        }
    }

}
