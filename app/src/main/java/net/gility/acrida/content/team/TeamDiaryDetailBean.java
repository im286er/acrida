package net.gility.acrida.content.team;

import net.gility.acrida.content.Entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("oschina")
public class TeamDiaryDetailBean extends Entity {

    @XStreamAlias("diary")
    private TeamDiary teamDiary;

    public TeamDiary getTeamDiary() {
        return teamDiary;
    }

    public void setTeamDiary(TeamDiary teamDiary) {
        this.teamDiary = teamDiary;
    }

}
