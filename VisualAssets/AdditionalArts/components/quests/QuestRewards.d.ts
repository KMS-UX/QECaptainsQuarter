/** Quest rewards display (Quest & Mission §13): XP / reputation / currency pills, item grid, choice-of-one reward row. */
export interface RewardItem {
  name?: string;
  rarity?: 'common'|'uncommon'|'rare'|'epic'|'legendary'|'mythic'|'exotic';
  image?: string;
}
export interface QuestRewardsProps {
  xp?: number | string;
  reputation?: number | string;
  currency?: number | string;
  items?: RewardItem[];
  choice?: RewardItem[];
  style?: React.CSSProperties;
}
export declare function QuestRewards(props: QuestRewardsProps): JSX.Element;
