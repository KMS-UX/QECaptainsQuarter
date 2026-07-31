Tracked-quest HUD list + quest status icons (Quest & Mission §10, §12).

```jsx
<QuestTracker quests={[
  {tier:'main', title:'The Core Relay', sub:'Reach the Core Relay', active:true},
  {tier:'side', title:'Lost Supply Cache', sub:'Recover Supplies (3/5)'},
  {tier:'world', title:'Void Rift Surge', timer:'02:45:18'},
]}/>

<QuestStatus state="in-progress"/>
```

`QuestTracker` rows are tier-colored on the left rail; pass `timer` instead of `sub` for time-limited events (renders gold). `QuestStatus` covers all 8 states — set `showLabel={false}` for icon-only use in lists.
