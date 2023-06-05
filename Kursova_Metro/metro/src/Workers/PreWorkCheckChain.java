package Workers;

import java.util.LinkedList;

public class PreWorkCheckChain implements  PreWorkCheck{
    private LinkedList<PreWorkCheck> checks;

    public PreWorkCheckChain() {
        checks = new LinkedList<>() {{
            add(new MedicalCheck()); add(new PsychologicalCheck()); add(new TechnicalKnowledgeCheck());
        }};
    }

    public boolean executeChecks(Worker worker) {
        for (PreWorkCheck check : checks) {
            if (!check.check(worker)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean check(Worker worker) {
        return executeChecks(worker);
    }
}
