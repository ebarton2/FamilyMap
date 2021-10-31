package ebarton2.byu.cs240.familymap.model;

public class Settings {
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean paternalLines;
    private boolean maternalLines;
    private boolean maleEvents;
    private boolean femaleEvents;

    private static Settings _instance;

    private Settings() {
        lifeStoryLines = true;
        familyTreeLines = true;
        spouseLines = true;
        paternalLines = true;
        maternalLines = true;
        maleEvents = true;
        femaleEvents = true;
    }

    public static Settings instance() {
        if(_instance == null) {
            _instance = new Settings();
        }
        return _instance;
    }

    public void clear() { _instance = new Settings(); }

    public void setLifeStoryLines(boolean click) { _instance._setLifeStoryLines(click); }

    private void _setLifeStoryLines(boolean click) { lifeStoryLines = click; }

    public boolean getLifeStoryLines() {return _instance._getLifeStoryLines(); }

    private boolean _getLifeStoryLines() { return lifeStoryLines; }



    public void setFamilyTreeLines(boolean click) { _instance._setFamilyTreeLines(click); }

    private void _setFamilyTreeLines(boolean click) { familyTreeLines = click; }

    public boolean getFamilyTreeLines() { return _instance._getFamilyTreeLines(); }

    private boolean _getFamilyTreeLines() { return familyTreeLines; }


    public void setSpouseLines(boolean click) { _instance._setSpouseLines(click); }

    private void _setSpouseLines(boolean click) { spouseLines = click; }

    public boolean getSpouseLines() { return _instance._getSpouseLines(); }

    private boolean _getSpouseLines() { return spouseLines; }


    public void setPaternalLines(boolean click) { _instance._setPaternalLines(click); }

    private void _setPaternalLines(boolean click) { paternalLines = click; }

    public boolean getPaternalLines() { return _instance._getPaternalLines(); }

    private boolean _getPaternalLines() { return paternalLines; }



    public void setMaternalLines(boolean click) { _instance._setMaternalLines(click); }

    private void _setMaternalLines(boolean click) { maternalLines = click; }

    public boolean getMaternalLines() { return _instance._getMaternalLines(); }

    private boolean _getMaternalLines() { return maternalLines; }



    public void setMaleEvents(boolean click) { _instance._setMaleEvents(click); }

    private void _setMaleEvents(boolean click) { maleEvents = click; }

    public boolean getMaleEvents() { return _instance._getMaleEvents(); }

    private boolean _getMaleEvents() { return maleEvents; }


    public void setFemaleEvents(boolean click) { _instance._setFemaleEvents(click); }

    private void _setFemaleEvents(boolean click) { femaleEvents = click; }

    public boolean getFemaleEvents() { return _instance._getFemaleEvents(); }

    private boolean _getFemaleEvents() { return femaleEvents; }
}
