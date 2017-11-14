
package org.eclipse.ceylon.common.tools.help;

import org.eclipse.ceylon.common.tool.ArgumentModel;
import org.eclipse.ceylon.common.tool.OptionModel;
import org.eclipse.ceylon.common.tool.WordWrap;
import org.eclipse.ceylon.common.tool.OptionModel.ArgumentType;
import org.eclipse.ceylon.common.tools.help.model.DescribedSection;
import org.eclipse.ceylon.common.tools.help.model.Doc;
import org.eclipse.ceylon.common.tools.help.model.Option;
import org.eclipse.ceylon.common.tools.help.model.OptionsSection;
import org.eclipse.ceylon.common.tools.help.model.SubtoolVisitor;
import org.eclipse.ceylon.common.tools.help.model.SummarySection;
import org.eclipse.ceylon.common.tools.help.model.SynopsesSection;
import org.eclipse.ceylon.common.tools.help.model.Synopsis;
import org.eclipse.ceylon.common.tools.help.model.Visitor;
import org.tautua.markdownpapers.ast.Node;

public class PlainVisitor implements Visitor {

    private int numOptions;
    protected final WordWrap out;
    protected String ceylonName;
    boolean hadFirstArgument = false;
    private boolean hadOptions;
    private boolean suboptions;
    private boolean hasSubTools;
    private boolean skipInvocation;
    private boolean firstSynopsis;

    public PlainVisitor(WordWrap wrap) {
        super();
        this.out = wrap;
    }

    private void markdown(Node doc) {
        PlaintextMarkdownVisitor markdownVisitor = new PlaintextMarkdownVisitor(out);
        doc.accept(markdownVisitor);
    }

    @Override
    public void start(Doc doc) {
        ceylonName = doc.getInvocation();
        // Make sure this is initialized even when startSynopses() never gets called
        hasSubTools = doc.getSynopses().getSynopses().size() > 1;
        firstSynopsis = true;
    }

    @Override
    public void end(Doc doc) {
        out.flush();
    }

    private String multiplicity(ArgumentModel<?> argument, String name) {
        name = "<" + name + ">";
        if (argument.getMultiplicity().isMultivalued()) {
            name += "...";
        }
        return name;
    }
    
    @Override
    public void startSynopsis(Synopsis synopsis) {
        int indent = out.getIndentFirstLine();
        String invocation = synopsis.getInvocation();
        out.setIndentRestLines(indent + invocation.length() + 1);
        if (!hasSubTools || firstSynopsis) {
            out.append(invocation);
        } else {
            out.append("*");
        }
        skipInvocation = hasSubTools && !firstSynopsis;
        hadFirstArgument = false;
        hadOptions = false;
    }
    
    @Override
    public void endSynopsis(Synopsis synopsis) {
        out.newline();
        if (hasSubTools) {
            out.newline();
        }
    }

    @Override
    public void visitSynopsisArgument(ArgumentModel<?> argument) {
        if (skipInvocation) {
            return;
        }
        if (hadOptions && !hadFirstArgument) {
            out.append(" [--]");
            hadFirstArgument = true;
        }
        
        out.append(" ");
        String name = argument.getName();
        if (!argument.getMultiplicity().isRequired()) {
            out.append("[");
        }
        out.append("<" + name);
        if (argument.getMultiplicity().isMultivalued()) {
            out.append("...");
        }
        out.append(">");
        if (!argument.getMultiplicity().isRequired()) {
            out.append("]");
        }
    }

    @Override
    public void visitSynopsisSubtool(SubtoolVisitor.ToolModelAndSubtoolModel option) {
        String name = option.getModel().getName();
        if (hasSubTools) {
            if (firstSynopsis) {
                out.append(" ");
                out.append(CeylonHelpToolMessages.msg("synopsis.subtool.commands"));
                out.newline();
                out.newline();
                out.append(CeylonHelpToolMessages.msg("synopsis.subtool.list"));
                out.newline();
                out.newline();
                out.append("*");
            }
            out.append(" ");
        } else {
            out.append(" ");
        }
        out.append(name);
        firstSynopsis = false;
        skipInvocation = false;
    }
    
    @Override
    public void visitSynopsisOption(OptionModel<?> option) {
        if (skipInvocation) {
            return;
        }
        hadOptions = true;
        out.append(" ");
        final ArgumentModel<?> argument = option.getArgument();
        if (!argument.getMultiplicity().isRequired()) {
            out.append("[");
        }
        if (option.getLongName() != null) {
            out.append("--" + option.getLongName());
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                out.append("=");
                out.append(multiplicity(argument, argument.getName()));
            } else if (option.getArgumentType() == ArgumentType.OPTIONAL) {
                out.append("[=");
                out.append(multiplicity(argument, argument.getName()));
                out.append("]");
            }
        } else {
            out.append("-" + option.getShortName());
            if (option.getArgumentType() == ArgumentType.REQUIRED) {
                out.append(" ");
                out.append(multiplicity(argument, argument.getName()));
            }
        }
        if (!argument.getMultiplicity().isRequired()) {
            out.append("]");
        }
    }
    
    
    @Override
    public void startSynopses(SynopsesSection synopsesSection) {
        out.append(synopsesSection.getTitle().toUpperCase()).newline().newline();
        out.setIndent(8);
        hasSubTools = synopsesSection.getSynopses().size() > 1;
        firstSynopsis = true;
    }
    
    @Override
    public void endSynopses(SynopsesSection synopsesSection) {
        out.setIndent(0);
        out.newline();
        out.newline();   
    }
    
    @Override
    public void startOptions(OptionsSection optionsSection) {
        if (suboptions) {
            out.setIndent(3);
        }
        markdown(optionsSection.getTitle());
        out.setIndent(8);
        suboptions = true;
    }

    @Override
    public void visitOption(Option option) {
        String shortName = option.getShortName();
        String longName = option.getLongName();
        String argumentName = option.getArgumentName();
        ArgumentType argumentType = option.getOption().getArgumentType();
        numOptions++;
        out.append(longName);
        if (argumentType == ArgumentType.OPTIONAL) {
            out.append("[");
        }
        if (argumentType != ArgumentType.BOOLEAN) {
            out.append("=<" + argumentName + ">");
        }
        if (argumentType == ArgumentType.OPTIONAL) {
            out.append("]");
        }
        if (shortName != null) {
            out.append(", ");
            out.append(shortName);
            if (argumentType == ArgumentType.REQUIRED) {
                out.append(" ");
                out.append("<" + argumentName + ">");
            }
        }
        
        out.setIndent(12);
        out.newline();
        markdown(option.getDescription());    
        out.newline();
        out.setIndent(8);
    
    }

    @Override
    public void endOptions(OptionsSection optionsSection) {
        if(numOptions == 0) {
            out.append(ceylonName+" has no options").newline();
        }
        out.setIndent(0);
        out.newline();
    }
    
    @Override
    public void visitAdditionalSection(DescribedSection describedSection) {
        describedSection(describedSection);    
    }

    private void describedSection(DescribedSection describedSection) {
        markdown(describedSection.getTitle());
        markdown(describedSection.getDescription());
        
        for (DescribedSection subsection : describedSection.getSubsections()) {
            describedSection(subsection);
        }
        
        out.setIndent(0);
        out.newline();
    }

    @Override
    public void visitSummary(SummarySection summarySection) {
        markdown(summarySection.getTitle());
        markdown(Markdown.markdown("`" + ceylonName + "` - " + summarySection.getSummary()));
        out.setIndent(0);
        out.newline();
    }

    @Override
    public void visitDescription(DescribedSection descriptionSection) {
        describedSection(descriptionSection);
    }


}
