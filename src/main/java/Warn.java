import net.dv8tion.jda.api.entities.Member;

import java.util.ArrayList;

public class Warn {

    ArrayList<String> warnCauses = new ArrayList<>();
    Member member;
    int warns;

    public Warn(String cause, Member member) {
        warns++;
        this.member = member;
        warnCauses.add(cause);
    }

    public void newWarn(String cause) {
        warns++;
        warnCauses.add(cause);
    }

    @Override
    public String toString() {
        return member.getEffectiveName();
    }
}
