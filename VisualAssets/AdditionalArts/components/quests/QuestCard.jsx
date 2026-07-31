import React from 'react';
const TIERS={main:{c:'var(--quest-main)',label:'MAIN STORY'},side:{c:'var(--quest-side)',label:'SIDE QUEST'},faction:{c:'var(--quest-faction)',label:'FACTION QUEST'},world:{c:'var(--quest-world)',label:'WORLD EVENT'},daily:{c:'var(--quest-daily)',label:'DAILY QUEST'},hidden:{c:'var(--quest-hidden)',label:'HIDDEN QUEST'}};
export function QuestCard({tier='main',title,description,objective,progress,progressMax,level,image,footer,width=200,style}){
  const t=TIERS[tier]||TIERS.main;
  return <div style={{width,background:'var(--surface-panel)',border:`1px solid ${t.c}`,borderRadius:'var(--radius-1)',overflow:'hidden',boxShadow:'var(--shadow-panel)',display:'flex',flexDirection:'column',...style}}>
    <div style={{background:t.c,color:'var(--void-0)',fontFamily:'var(--font-display)',fontSize:9,fontWeight:700,letterSpacing:'var(--tracking-caps)',textAlign:'center',padding:'5px 0'}}>{t.label}</div>
    {image&&<img src={image} alt="" style={{width:'100%',height:86,objectFit:'cover',imageRendering:'pixelated'}}/>}
    <div style={{padding:10,display:'flex',flexDirection:'column',gap:6,flex:1}}>
      <div style={{fontFamily:'var(--font-display)',fontSize:12,fontWeight:700,color:'var(--fg-1)'}}>{title}</div>
      {description&&<div style={{fontSize:11,color:'var(--text-muted)',lineHeight:1.45}}>{description}</div>}
      {objective&&<div style={{fontSize:11,color:'var(--fg-2)'}}>Objective: {objective}</div>}
      {progressMax!=null&&<div style={{display:'flex',alignItems:'center',gap:6}}>
        <div style={{flex:1,height:6,background:'var(--bar-track)',border:'1px solid var(--border-panel)'}}><div style={{width:(progress/progressMax*100)+'%',height:'100%',background:t.c}}></div></div>
        <span style={{fontSize:10,color:'var(--text-muted)'}}>{progress}/{progressMax}</span></div>}
      <div style={{marginTop:'auto',display:'flex',justifyContent:'space-between',alignItems:'center'}}>
        <span style={{fontSize:10,color:'var(--text-faint)'}}>{footer}</span>
        {level&&<span style={{fontSize:10,color:t.c}}>Lv. {level}</span>}
      </div>
    </div>
  </div>;
}
