package no.haagensoftware.kontize.db.dao;

import com.google.gson.JsonPrimitive;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.kontize.models.Talk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 1/28/14.
 */
public class TalkDao {
    private StoragePlugin storagePlugin;

    public TalkDao(StoragePlugin storagePlugin) {
        this.storagePlugin = storagePlugin;
    }

    public List<Talk> getTalks() {
        List<Talk> talks = new ArrayList<>();

        for (SubCategoryData subCategoryData : storagePlugin.getSubCategories("talks")) {
            talks.add(convertSubcategoryToTalk(subCategoryData));
        }

        return talks;
    }

    public List<Talk> getTalksForUser(String userId) {
        List<Talk> talksForUser = new ArrayList<>();

        for (Talk talk : getTalks()) {
            if (talk.getUserId().equals(userId)) {
                talksForUser.add(talk);
            }
        }

        return talksForUser;
    }

    public Talk getTalk(String subcategory) {
        Talk talk = null;

        SubCategoryData subCategoryData = storagePlugin.getSubCategory("talks", subcategory);

        if (subCategoryData != null) {
            talk = convertSubcategoryToTalk(subCategoryData);
        }

        return talk;
    }

    public Talk convertSubcategoryToTalk(SubCategoryData subCategoryData) {
        Talk talk = new Talk();
        talk.setAbstractId(subCategoryData.getId());
        talk.setTalkIntendedAudience(subCategoryData.getValueForKey("talkIntendedAudience"));
        talk.setComments(subCategoryData.getValueForKey("comments"));
        talk.setOutline(subCategoryData.getValueForKey("outline"));
        talk.setParticipantRequirements(subCategoryData.getValueForKey("participantRequirements"));
        talk.setTalkAbstract(subCategoryData.getContent());
        talk.setTitle(subCategoryData.getValueForKey("title"));
        talk.setTalkType(subCategoryData.getValueForKey("talkType"));
        talk.setTopics(subCategoryData.getValueForKey("topics"));
        talk.setUserId(subCategoryData.getValueForKey("userId"));
        return talk;
    }

    public void storeTalk(Talk talk, String userId) {
        SubCategoryData subCategoryData = new SubCategoryData();
        subCategoryData.setId(talk.getAbstractId());
        subCategoryData.getKeyMap().put("abstractId", new JsonPrimitive(talk.getAbstractId()));

        subCategoryData.setContent(talk.getTalkAbstract());
        subCategoryData.getKeyMap().put("userId", new JsonPrimitive(userId));

        if (talk.getTalkIntendedAudience() != null) {
            subCategoryData.getKeyMap().put("talkIntendedAudience", new JsonPrimitive(talk.getTalkIntendedAudience()));
        }

        if (talk.getComments() != null) {
            subCategoryData.getKeyMap().put("comments", new JsonPrimitive(talk.getComments()));
        }

        if (talk.getOutline() != null) {
            subCategoryData.getKeyMap().put("outline", new JsonPrimitive(talk.getOutline()));
        }

        if (talk.getParticipantRequirements() != null) {
            subCategoryData.getKeyMap().put("participantRequirements", new JsonPrimitive(talk.getParticipantRequirements()));
        }

        if (talk.getTitle() != null) {
            subCategoryData.getKeyMap().put("title", new JsonPrimitive(talk.getTitle()));
        }

        if (talk.getTalkType() != null) {
            subCategoryData.getKeyMap().put("talkType", new JsonPrimitive(talk.getTalkType()));
        }

        if (talk.getTopics() != null) {
            subCategoryData.getKeyMap().put("topics", new JsonPrimitive(talk.getTopics()));
        }

        storagePlugin.setSubCategory("talks", subCategoryData.getId(), subCategoryData);
    }

    public void deleteTalk(String talkId) {
        storagePlugin.deleteSubcategory("talks", talkId);
    }
}
