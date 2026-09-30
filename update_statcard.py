import re

with open("src/GUI.java", "r") as f:
    gui_text = f.read()

# Add overloaded statCard method
statcard_def = r'JPanel statCard\(\s*String heading,\s*String value,\s*String caption,\s*Color accent\)\s*\{'

new_statcard_def = """
    JPanel statCard(String heading, String value, String caption, Color accent) {
        return statCard(heading, value, caption, accent, null);
    }

    JPanel statCard(String heading, String value, String caption, Color accent, String icon) {
"""

gui_text = re.sub(statcard_def, new_statcard_def.strip(), gui_text)

# Find JLabel h = new JLabel(heading); and replace it
h_def = r'JLabel h =\s*new JLabel\(\s*heading\s*\);'
new_h_def = 'JLabel h = new JLabel(icon != null ? icon + "  " + heading : heading);'

gui_text = re.sub(h_def, new_h_def, gui_text)

# Update createStatsRow() calls to use statCard with icons
stats_calls = [
    ('stats.add(statCard("ACTIVE MEMBERS", String.valueOf(activeMembers), "+2 this week", INK));',
     'stats.add(statCard("ACTIVE MEMBERS", String.valueOf(activeMembers), "+2 this week", INK, "👥"));'),
    ('stats.add(statCard("ON LOAN", String.valueOf(onLoan), "+5 this week", SAGE));',
     'stats.add(statCard("ON LOAN", String.valueOf(onLoan), "+5 this week", SAGE, "📖"));'),
    ('stats.add(statCard("DUE TODAY", String.valueOf(dueToday), "+0 this week", GOLD));',
     'stats.add(statCard("DUE TODAY", String.valueOf(dueToday), "+0 this week", GOLD, "⏰"));'),
    ('stats.add(statCard("OVERDUE BOOKS", String.valueOf(overdue), "+1 this week", TERRACOTTA));',
     'stats.add(statCard("OVERDUE BOOKS", String.valueOf(overdue), "+1 this week", TERRACOTTA, "⚠️"));')
]

for old, new in stats_calls:
    gui_text = gui_text.replace(old, new)

with open("src/GUI.java", "w") as f:
    f.write(gui_text)

print("statCard updated")
