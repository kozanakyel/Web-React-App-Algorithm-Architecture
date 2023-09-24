import classes from './Card.module.css';

const Card = (props) => {
  return (
    <section
      className={`${classes.card} ${props.className ? props.className : ''}`}
    >
      {props.children}{/** for new components loaction */}
    </section>
  );
};

export default Card;
