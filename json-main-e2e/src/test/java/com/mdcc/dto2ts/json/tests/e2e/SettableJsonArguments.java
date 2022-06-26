package com.mdcc.dto2ts.json.tests.e2e;

import com.mdcc.dto2ts.json.main.JsonArguments;

public class SettableJsonArguments extends JsonArguments
{
    @Override
    public void setSuffixToRemove(String suffixToRemove)
    {
        super.setSuffixToRemove(suffixToRemove);
    }

    @Override
    public void setPrefixToRemove(String prefixToRemove)
    {
        super.setPrefixToRemove(prefixToRemove);
    }

    @Override
    public void setOutputFolder(String outputFolder)
    {
        super.setOutputFolder(outputFolder);
    }

    @Override
    public void setCreateVisitor(boolean createVisitor)
    {
        super.setCreateVisitor(createVisitor);
    }

    @Override
    public void setVisitableName(String visitableName)
    {
        super.setVisitableName(visitableName);
    }

    @Override
    public void setVisitablePath(String visitablePath)
    {
        super.setVisitablePath(visitablePath);
    }

    @Override
    public void setVisitorName(String visitorName)
    {
        super.setVisitorName(visitorName);
    }

    @Override
    public void setDomainFile(String domainFile)
    {
        super.setDomainFile(domainFile);
    }

    @Override
    public void setDomainPrefix(String domainPrefix)
    {
        super.setDomainPrefix(domainPrefix);
    }

    @Override
    public void setDomainFilePrefix(String domainFilePrefix)
    {
        super.setDomainFilePrefix(domainFilePrefix);
    }

    @Override
    public void setUidPrefix(String uidPrefix)
    {
        super.setUidPrefix(uidPrefix);
    }

    @Override
    public void setThreshold(double threshold)
    {
        super.setThreshold(threshold);
    }

    @Override
    public void setJson(String json)
    {
        super.setJson(json);
    }

    @Override
    public void setRootModel(String rootModel)
    {
        super.setRootModel(rootModel);
    }
}
