package net.gility.acrida.content.team;

import java.util.ArrayList;
import java.util.List;

import net.gility.acrida.content.Entity;
import net.gility.acrida.content.ListEntity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Team成员列表
 * 
 * @author kymjs
 * 
 */
@XStreamAlias("oschina")
public class TeamMemberList extends Entity implements ListEntity<TeamMember> {

    private static final long serialVersionUID = 1L;

    @XStreamAlias("members")
    private List<TeamMember> list = new ArrayList<TeamMember>();

    public void setList(List<TeamMember> list) {
        this.list = list;
    }

    @Override
    public List<TeamMember> getList() {
        return list;
    }

}
