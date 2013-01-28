/*******************************************************************************
 * Copyright 2013 by Helge Walter
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
 ******************************************************************************/

package de.bewalt.intellij.plugin.cvsinfo;

import com.intellij.cvsSupport2.CvsUtil;
import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ProjectViewNodeDecorator;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packageDependencies.ui.PackageDependenciesNode;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.Nullable;
import org.netbeans.lib.cvsclient.admin.Entry;

/**
 * This file is part of CVSInfo Plug-In.
 * User: walter
 * Date: 24.01.13
 * Time: 16:18
 * <p/>
 * This node decorator provides sticky tag information to project directory or file nodes under CVS control.
 */
public class CvsInfoTreeNodeDecorator implements ProjectViewNodeDecorator
{

  @Override
  public void decorate(ProjectViewNode node, PresentationData data)
  {
    String stickyTag = getStickyTagFor(node);

    if (stickyTag != null)
    {
      String parentStickyTag = getStickyTagFor(node.getParent());
      if (!stickyTag.equals(parentStickyTag))
      {
        if (data.getColoredText().isEmpty() && data.getPresentableText() != null)
        {
          data.addText(data.getPresentableText(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
        data.addText(" <" + stickyTag + ">", SimpleTextAttributes.GRAY_ATTRIBUTES);
      }
    }
  }

  @Nullable
  private String getStickyTagFor(@Nullable AbstractTreeNode node)
  {
    String stickyTag = null;
    VirtualFile file = null;

    if (node instanceof PsiDirectoryNode)
    {
      file = ((PsiDirectoryNode) node).getValue().getVirtualFile();

      if (file != null && CvsUtil.fileIsUnderCvs(file))
      {
        stickyTag = CvsUtil.getStickyTagForDirectory(file);
      }
    }
    else if (node instanceof PsiFileNode)
    {
      file = ((PsiFileNode) node).getValue().getVirtualFile();

      if (file != null && CvsUtil.fileIsUnderCvs(file))
      {
        Entry entry = CvsUtil.getEntryFor(file);

        if (entry != null)
        {
          stickyTag = entry.getStickyTag();
        }
      }
    }


    return stickyTag;
  }

  @Override
  public void decorate(PackageDependenciesNode node, ColoredTreeCellRenderer cellRenderer)
  {
    // do nothing
  }
}
