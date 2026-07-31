/** HUD toasts. Notification: stacked teal-edged event lines ("New Quest Added", "Level Up!"). LootPopup: purple-glow pickup list with counts. */
export interface NotificationProps { items?: string[]; style?: React.CSSProperties; }
export declare function Notification(props: NotificationProps): JSX.Element;
export interface LootItem { name: string; count?: number; color?: string; }
export interface LootPopupProps { items?: LootItem[]; style?: React.CSSProperties; }
export declare function LootPopup(props: LootPopupProps): JSX.Element;
