/** Message boxes from the HUD sheet §07: dialogue (blue, portrait), system (green), warning (red glow), confirm (neutral + action buttons). */
export interface DialogueBoxProps {
  kind?: 'dialogue' | 'system' | 'warning' | 'confirm';
  speaker?: string;
  portrait?: string;
  children?: React.ReactNode;
  actions?: React.ReactNode;
  style?: React.CSSProperties;
}
export declare function DialogueBox(props: DialogueBoxProps): JSX.Element;
