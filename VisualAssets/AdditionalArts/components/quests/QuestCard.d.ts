/** Quest card (Quest & Mission sheet §05): tier-colored header strip + border, optional key art, objective, progress bar, level gate. */
export interface QuestCardProps {
  tier?: 'main'|'side'|'faction'|'world'|'daily'|'hidden';
  title?: string;
  description?: string;
  objective?: string;
  progress?: number;
  progressMax?: number;
  level?: string | number;
  image?: string;
  footer?: React.ReactNode;
  width?: number | string;
  style?: React.CSSProperties;
}
export declare function QuestCard(props: QuestCardProps): JSX.Element;
