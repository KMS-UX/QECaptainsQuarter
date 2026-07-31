/** Tracked-quest HUD panel (Quest & Mission §10) + QuestStatus state icon (§12). */
export interface TrackedQuest {
  tier?: 'main'|'side'|'faction'|'world'|'daily'|'hidden';
  title: string;
  sub?: string;
  timer?: string;
  active?: boolean;
}
export interface QuestTrackerProps {
  title?: string;
  quests?: TrackedQuest[];
  width?: number;
  style?: React.CSSProperties;
}
export declare function QuestTracker(props: QuestTrackerProps): JSX.Element;
export interface QuestStatusProps {
  state?: 'available'|'accepted'|'in-progress'|'ready'|'completed'|'failed'|'abandoned'|'tracked'|'untracked';
  showLabel?: boolean;
  size?: number;
  style?: React.CSSProperties;
}
export declare function QuestStatus(props: QuestStatusProps): JSX.Element;
