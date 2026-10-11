import axios from 'axios';
export const api=axios.create({baseURL:import.meta.env.VITE_API_URL||'http://localhost:8080/api'});api.interceptors.request.use(c=>{const t=localStorage.getItem('joblens_token');if(t)c.headers.Authorization=`Bearer ${t}`;return c});
export type Job={id:number;title:string;company:string;location:string;description:string;requiredSkills:string;postedAt:string};
export type Match={jobId:number;jobTitle:string;score:number;scoreExplanation:string;matchedSkills:string[];missingSkills:string[];evidence:string[];nextSteps:string[]};export type Auth={token:string;userId:number;email:string;fullName:string};
export type Application={id:number;job:Job;status:string;notes?:string;updatedAt:string};
export type Profile={headline?:string;location?:string;preferredLocations?:string;experienceLevel?:string;skills:string[];education?:string};
